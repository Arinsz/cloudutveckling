# AWS CodeCommit
> **Note:** Before starting, make sure the following prerequisites are met:

* AWS CLI is installed and properly configured with the necessary permissions to interact with AWS services.

* A CodeCommit repository has been created for storing your code.



---
## Introduction

AWS CodeCommit is a fully managed source control service provided by Amazon Web Services (AWS). It operates similarly to Git and is designed to handle source code, binary files, and other digital assets with high security and scalability.



### EC2-Setup:

Go to Amazon EC2 instances and launch one instance.

* **Instance name**: Write a name for your instance.
* **Instance type**: t3.micro
* **AMI**: Amazon Linux
* **IAM Role**: We need a role for this instance to have AmazonS3FullAccess & AmazonRDSFullAccess. If you don't have one you can create on in IAM/Roles.
* **Security group**: Make sure you have port 3306, 8080, 22 open in inbound rules.

Start the instance and login with SSH.

   ```bash
   sudo yum update -y
   sudo yum install java-21-amazon-corretto -y
   sudo yum install ruby
   sudo yum install wget
   wget https://aws-codedeploy-us-east-2.s3.us-east-2.amazonaws.com/latest/install
   chmod +x ./install
   sudo ./install auto
   sudo systemctl start codedeploy-agent
   sudo systemctl status codedeploy-agent
   ```



### MySQL Database Setup On Ec2 Linux :

  ```bash
  sudo wget https://dev.mysql.com/get/mysql80-community-release-el9-1.noarch.rpm 
  sudo dnf install mysql80-community-release-el9-1.noarch.rpm -y
  sudo rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2023
  sudo dnf install mysql-community-client -y
  sudo dnf install mysql-community-server -y
  sudo systemctl enable mysqld
  sudo systemctl start mysqld
  sudo systemctl status mysqld
   ```

* Now we need o create the database and users that can access this database.
 ```bash
sudo grep 'temporary password' /var/log/mysqld.log (for logging the first time)
sudo mysql -u root -p 
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password'; 
FLUSH PRIVILEGES;
CREATE DATABASE yourDbName;
GRANT ALL PRIVILEGES ON yourDbName.* TO 'root'@'localhost';
SHOW GRANTS FOR 'root'@'localhost';
   ```


* Set Enviroment Variables on EC2 for connecting to Database :
 ```bash
nano ~/.bashrc
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/yourDbName
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=yourPassword
export SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
save & exit
source ~/.bashrc
   ```

* Install Nginx for redirecting HTTP traffic to HTTPS 443 :

 ```bash
sudo yum install nginx (Server name ska vara EC2 Public IP adress)
sudo systemctl start nginx
sudo systemctl enable nginx
sudo nano /etc/nginx/conf.d/default.conf
 ```

* In the configuration file we need to add some code to redirect HTTP Traffic to HTTPS:

Nginx Configuration![Build Success](https://github.com/Distansakademin/cloudutveckling-spring-Arinsz/blob/main/src/main/resources/static/images/Github%20presentation%20Images/nginx.jpg))



 ```bash
sudo nginx -t ( to test the config)
sudo systemctl restart nginx
 ```
* Make sure your EC2-Instance allow HTTPS traffik on port 443.

* Make sure you have your instance public IP on Allowed Callback URLS in Cognito.

* In the class CognitoLogoutSucessHandler change homePageUrl to :     


```bash
    var homePageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .scheme("https")  // Force HTTPS scheme
                .path("/home")
                .build()
                .toUriString();

```



---
### Source:

- **Upload Your Code**: Open the terminal in IntelliJ or navigate to your repository directory in the terminal.

   ```bash
   git add .
   git commit -m "Upload to CodeCommit Repository"
   git remote add origin <codecommit-repository-URL>
   git push -u origin main
  
- **Switching Your Remote Back to GitHub**

   ```
   git remote remove origin
   git remote add origin <github-repo-url>
   git remote -v  (Verify the new remote: To ensure the new remote is correctly set up:)
   ```


---
### Build:

Navigate to **Codebuild/Build Projects**

- **Project name** : Enter project name.
- **Source** : Sourceprovider = Aws Codecommit, Repository = Your CodeCommit Repository.
- **Branch**: main.
- **Service role**: Existing service role, you should have a service role with AmazonEC2FullAccess, AmazonS3FullAccess , CloudWatchLogsFullAccess. If you don't have one you can create one in IAM/Roles.
- **Buildspec**: Use a buildspec file.
- **Artifacts**: no artifacts, we create this in buildspec.
- **Start build**: Press start build to verify that the build is succeeded. 


![Build Success](https://github.com/Distansakademin/cloudutveckling-spring-Arinsz/blob/c29931e49a45d9d5e123d867a77edaf0d9372d45/src/main/resources/static/images/Github%20presentation%20Images/BuildSuccess.jpg?raw=true)

---
### Deploy:

Navigate to **Codedeploy/Applications** and press Create Application.

* **Application name**:  Enter an application name.
* **Compute platform**: EC2/on-premises.
* **Deployment group name**: Enter a deployment group name.
* **Service role**: You need to have a servicerole with AmazonS3FullAccess, AWSCodeDeployRole. If you don't have one you can go to IAM/Roles and create one.
* **Environment configuration**: Amazon EC2 Instances
* **Tag group**: key = name, value = yourEc2Instance

Click on  **Create deployment group** 

---

### Codepipeline:

* **Create a new  pipeline.**
* **Pipeline name**: Enter a name for your pipeline.
* **Service role**: Create new service role, enter your role name.
* **Source provider**:  AWS CodeCommit.
* **Repository name**:  Your CodeCommit Repository for this application.
* **Branch name**:  main.
* **Build provider**:  Aws Codebuild.
* **Project name**:  Choose the build project that you have already created in the AWS CodeBuild.
* **Application name**: AWS CodeDeploy.
* **Deploy provider**: Choose the application that you have already created in the AWS CodeDeploy.
* **Deployment group**: Choose the deployment group that you have already created in the AWS CodeDeploy.

![Build Success](https://github.com/Distansakademin/cloudutveckling-spring-Arinsz/blob/main/src/main/resources/static/images/Github%20presentation%20Images/Deploy.jpg)


### Run the application 

In your EC2, Change directory to Server 

```bash
 cd server
 ls
 java -jar nameofyourjarfile.jar 
```

![EC2](https://github.com/Distansakademin/cloudutveckling-spring-Arinsz/blob/main/src/main/resources/static/images/Github%20presentation%20Images/ec2.jpg)