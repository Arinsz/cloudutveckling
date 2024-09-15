## Jenkins Deployment guide.



### EC2-Setup:
Go to Amazon EC2 instances and launch one instance.

* **Instance name**: Write a name for your instance.
* **Instance type**: t3.micro
* **AMI**: Amazon Linux
* **Security group**: Make sure you have port 3306, 8080, 22, 443, open in inbound rules.
* **redirect-uri**: Change the redirect uri to HTTPS, and the EC2 public ip in application.yml. And in home template change the login button to https and EC2 public ip. And in Cognito add callback URL, and signout URL with EC2 Public IP and HTTPS.

### Login to your instance with SSH and install java: 
   ```bash
   sudo yum update -y
   sudo yum install java-21-amazon-corretto -y
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
sudo yum install nginx (Server name should be EC2 Public IP adress)
sudo systemctl start nginx
sudo systemctl enable nginx
sudo mkdir -p /etc/ssl/private
sudo chmod 700 /etc/ssl/private
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/ssl/private/nginx-selfsigned.key -out /etc/ssl/certs/nginx-selfsigned.crt
sudo nano /etc/nginx/conf.d/default.conf
 ```

* In the configuration file we need to add some code to redirect HTTP Traffic to HTTPS:

Nginx Configuration![Conf](https://github.com/Arinsz/cloudutveckling/blob/main/src/main/resources/static/images/Github%20presentation%20Images/nginx.jpg)


---

## Jenkins Setup Guide

### Required Plugins

Ensure the following Jenkins plugins are installed:

- **Publish Over SSH Plugin**
- **SSH Agent Plugin**
- **Pipeline Plugin**
- **Maven Integration Plugin**
- **Git Plugin**
- **Credentials Plugin**

### Configure Credentials

1. Go to **Credentials** in Jenkins.
2. Add a global credential to connect via SSH to your EC2 server.
3. Go to Github/Settings/Developer Settings/Personal Access Tokens(Classic) and create one.
4. Add this to global credential to be able to access your Github repository with jenkins.

### Configure SSH Server

1. Go to **Manage Jenkins** > **System**.
2. Scroll down to **SSH Servers**.
3. Enter the details for your EC2 server.

   ![SSH Servers](https://github.com/Arinsz/cloudutveckling/blob/main/src/main/resources/static/images/Github%20presentation%20Images/JenkinsSSHBild.jpg)

### Create a Freestyle Project

1. Go to the **Dashboard**, then click **New Item**.
2. Create a **Freestyle Project**.

#### Source Code Management

- **Select Git**.
- **Repository URL**: Enter your repository URL.
- **Credentials**: Choose the credentials you created.
- **Branches to build**: `Main`
- **Build Triggers**: Check **GitHub hook trigger for GITScm polling**.
- **Build Environment**: Check **Delete workspace before build starts**.

#### Build Steps

1. **Invoke top-level Maven targets**:
 - **Version**: Default
 - **Goals**: `clean install`

#### Post-build Actions

1. **Send build artifacts over SSH**:
 - **SSH Server**: Enter the name of your EC2 instance.
 - **Source files**: `target/*.jar`
 - **Remote directory**: `/home/ec2-user`

2. Click **Build Now**.


![SSH Servers](https://github.com/Arinsz/cloudutveckling/blob/main/src/main/resources/static/images/Github%20presentation%20Images/JenkinsBuildSuccess.jpg)


### Running the JAR File

Once the build is complete, navigate to the target directory on your EC2 instance:

```bash
cd /home/ec2-user/target
java -jar techstore-0.0.1-SNAPSHOT.jar
```


![App-running](https://github.com/Arinsz/cloudutveckling/blob/main/src/main/resources/static/images/Github%20presentation%20Images/JenkinsJarFile.jpg)