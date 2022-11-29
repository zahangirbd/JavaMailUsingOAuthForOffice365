# JavaMailUsingOAuthForOffice365 &middot; [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square)](https://github.com/your/your-project/blob/master/LICENSE)
Send and read emails using OAuth2 in Java for Microsoft Office365 or Outlook

## Objectives
A very few resources with a complete set of MS configurations and a sample project are available online to send and/or read emails through Microsoft Office365 or Outlook using OAuth2 in Java.
Thus, the primary targets are:
  - to provide the guidelines for the MS configurations for sending emails through Microsoft Office365 or Outlook in Java using OAuth2 (i.e., using SMTP AUTH command)
  - to list down the the MS configurations for reading emails from Microsoft Office365 or Outlook in Java using OAuth2

### Pre-requisites: Setup Email SMTP Settings for Office365 with OAuth2 Authentication
1. Register an (either web/desktop/mobile) application into [Azure Active Directory (AAD)](https://portal.azure.com) first.
From [AAD portal](https://portal.azure.com), we need to collect the following two IDs primarily:
	```shell
	clinetId or applicationId
	tenantId
	``` 
	
2. We need to provide access some Microsoft Graph APIs to the registered app.
  