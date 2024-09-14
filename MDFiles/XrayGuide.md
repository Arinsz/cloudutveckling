> **Note:** ensure that the AWS CLI is installed and configured with appropriate permissions to interact with AWS services using Powershell.

### Running AWS X-Ray Locally

* You can run the AWS X-Ray daemon locally to test and trace your application during development.

* Download the AWS X-Ray Daemon:
Download the X-Ray daemon from the AWS X-Ray Daemon Downloads Page and install it for your operating system.
* https://docs.aws.amazon.com/xray/latest/devguide/xray-daemon.html
* Once downloaded, extract the contents of the .zip file to a directory on your local machine.  
* Run the following command to start the X-Ray daemon:

```
.\xray_windows.exe -o
```

#### View Traces in AWS X-Ray Console

* Go to the AWS X-Ray Console

Navigate to the Service Map section to see the traces collected by the X-Ray daemon.
The service map provides a visual representation of your application's services and their interactions, helping you to identify performance bottlenecks and troubleshoot issues.
Review Trace Data:
Use the X-Ray Console to analyze trace data, review performance metrics, and understand request latencies.

![Xray](https://github.com/Distansakademin/cloudutveckling-spring-Arinsz/blob/main/src/main/resources/static/images/Github%20presentation%20Images/Xray.jpg)


### Annotations :

* @XRayTimed: This annotation is used on specific methods to create X-Ray segments for those methods. The segmentName attribute specifies the name of the segment that will be created in X-Ray. 

* Subsegments: If you want to add more granularity, you can create additional subsegments manually using the AWS X-Ray SDK. This allows you to trace specific operations within a method.

* Error Handling: Ensure that exceptions are handled properly so that they are also recorded in X-Ray traces. AWS X-Ray will automatically capture exceptions and include them in the traces.

* Custom Segments: For more advanced usage, you might create custom segments and subsegments using the AWS X-Ray SDK directly in your application code.

---

### Setting Up AWS X-Ray with EC2

##### Step 1: Set Up IAM Role for EC2


* Go to the IAM Management Console.
Create a new role and select AWS service as the trusted entity type.
Choose EC2 as the use case.
Attach the AWSXRayDaemonWriteAccess policy to the role. This policy allows the EC2 instance to send trace data to AWS X-Ray.
Complete the role creation process.




* Attach the IAM Role to Your EC2 Instance:
Go to the EC2 Management Console. Select your EC2 instance.
Choose Actions > Security > Modify IAM Role.
Select the IAM role you created and apply the changes. 


### Step 2: Install and Run the X-Ray Daemon on EC2
Connect to Your EC2 Instance:

Use SSH to connect to your EC2 instance.
Download the X-Ray Daemon:

```
wget https://s3.us-west-2.amazonaws.com/aws-xray-assets.us-west-2/xray-daemon/aws-xray-daemon-3.x.rpm
sudo yum install -y aws-xray-daemon-3.x.rpm
sudo systemctl start xray
sudo systemctl enable xray
sudo systemctl status xray
```

---