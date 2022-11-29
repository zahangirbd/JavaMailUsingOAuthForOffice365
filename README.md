# JavaMailUsingOAuthForOffice365 &middot; [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square)](https://github.com/your/your-project/blob/master/LICENSE)
Send and read emails using OAuth2 in Java for Microsoft Office365 or Outlook

## Objectives
A very few resources with a complete set of MS configurations and a sample project are available online to send and/or read emails through Microsoft Office365 or Outlook using OAuth2 in Java.
Thus, the primary targets are:
  - to provide the guidelines for the MS configurations for sending emails through Microsoft Office365 or Outlook in Java using OAuth2 (i.e., using SMTP AUTH command)
  - to list down the the MS configurations for reading emails from Microsoft Office365 or Outlook in Java using OAuth2

### Pre-requisites: Setup Email SMTP Settings for Office365 with OAuth2 Authentication
1. A step by step setup guideline is available [here](https://github.com/zahangirbd/JavaMailUsingOAuthForOffice365/blob/main/Setup-SMTP-Settings.md#setup-email-smtp-settings-for-office365-for-oauth2-authentication).

2. Once we complete the setup, we shall copy the following items from [Azure Portal](https://portal.azure.com/)

	```shell
	- Application (Client) ID
	- Directory (Tenant) ID
	```

### Setup the Java project
1. We can setup this project by any of the following two options 
	- Import this java project into IDE (e.g., eclipse) as a **gradle** project  
	- Prepare this java project for eclipse by following command and then open the project into IDE 
		```shell
		gradle eclipse
		``` 

2. Change the following configurations into **application.properties** under **src/resources**
We will need **Application (Client) ID**, **Directory (Tenant) ID**, **redirectUrl**, sender email address and receiver email addresses here
	```shell
	clinetId=<YOUR_REGISTERED_APP_CLIENT_ID>
	userName=<YOUR_OFFICE365_EMAIL_ADDRESS>
	authEndpointUrl=https://login.microsoftonline.com/<YOUR_TENANT_ID>/oauth2/v2.0/authorize
	scopes=offline_access,openId,https://outlook.office.com/SMTP.Send
	redirectUrl=http://localhost
	toAddresses=<TO_EMAIL_ADDRESSES>
	``` 

3. Run the following Java class file 
	```shell
		org.zahangirbd.email.EmailSender
	```
	- It will collect accessToken of OAuth2	using default browser with the given redirect URL
	- It will use the accessToken for SMTP AUTH for sending email 
	- It will send an test email to the provide **toAddresses** mentioned in **application.properties**
	