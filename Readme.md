# Arins Tech Store

> **Note:** I am continuously working on enhancing ArinsTechStore. Stay tuned for updates and new features!

![Build Success](https://github.com/Arinsz/cloudutveckling/blob/main/src/main/resources/static/images/Github%20presentation%20Images/hemsida1.jpg)
![Build Success](https://github.com/Arinsz/cloudutveckling/blob/main/src/main/resources/static/images/Github%20presentation%20Images/hemsida2.jpg)




---
## Introduction

Arins Tech Store is a Spring Boot application using AWS Cognito with a database on EC2(Linux AMI) based on your choice of deployment. This application is designed to manage tech products, with features including:

### Key Features

- **User Authentication and Authorization**: Powered by AWS Cognito, supporting roles like Admins for managing products.
- **Database Management**: Utilizes MySQL for data storage.
- **File Storage**: Integrates with AWS S3 for image uploads and storage.
- **Tracing and Monitoring**: Implements AWS X-Ray for request tracing and performance monitoring.
- **Backend**: Developed with Spring Boot, providing RESTful APIs and business logic.
- **Frontend**: Built with Thymeleaf templates, offering a user-friendly interface directly integrated with the Spring Boot backend.

---
> **Note:** You will need to do the AWS Cognito Setup and Create S3 Bucket Setup before Deployment Guides..
## Getting Started
* For deployment using AWS CodePipeline and Amazon RDS Database, refer to the [AWS CodePipeline Deployment Guide.](https://github.com/Arinsz/cloudutveckling/blob/main/AWS%20CodePipeline%20Deployment.md).

* For deployment using Jenkins, refer to the [Jenkins Pipeline Deployment Guide.](https://github.com/Arinsz/cloudutveckling/blob/main/JENKINS%20DEPLOYMENT.md).

* For running locally, refer to the [Running Locally Deployment Guide.](https://github.com/Arinsz/cloudutveckling/blob/main/Running%20Locally.md).
---

### AWS Cognito Setup
To enable user authentication and authorization with AWS Cognito, follow these steps:

1.  * Create a Cognito User Pool
    * Log in to the AWS Management Console. Navigate to Amazon Cognito and create a User Pool.
     Name your user pool 
    *  Configure the pool to use Cognito Hosted UI,  take note of the clientSecret, ClientId, userPoolId, clientName (you will need these for your application.yml).
    *  Create a userGroup (Admins):
   In the Cognito console, open your newly created User Pool.
   Navigate to the Groups section and create a group named Admins.
   This group will be used to give users admin rights to manage products.
   Give this group IAM role AmazonCognitoPowerUser.

---
### Create S3 Bucket Setup



* Login to AWS and create one **S3 Bucket,** In the **S3** **Service class** Change the String bucketName to your bucket name.
* Go to your bucket in AWS and then go to permissions and chose : Block all public access
  Off.

  
> **Note:** ensure that the AWS CLI is installed and configured with appropriate permissions to interact with AWS services using Powershell.

**Powershell S3 Setup :** 

  ```bash
  aws s3api create-bucket --bucket your-bucket-name --region eu-north-1 --create-bucket-configuration LocationConstraint=eu-north-1
  
  aws s3api put-public-access-block --bucket your-bucket-name --public-access-block-configuration BlockPublicAcls=false,IgnorePublicAcls=false,BlockPublicPolicy=false,RestrictPublicBuckets=false
   ```


#### Contact info test:

* Arin Sarafraz at Arin.Sz@hotmail.com
