# OpCon Agent-less via Bolt
Provides a Job Sub-type **Agentless(Bolt)** to drive agentless workloads on remote Windows and Linux systems using Bolt.

![diagrm](/docs/images/Connectors_overview.png)


Bolt is an open source orchestration tool that automates the manual work it takes to maintain your infrastructure. Use Bolt to automate tasks that you perform on an as-needed basis or as part of a greater orchestration workflow. For example, you can use Bolt to patch and update systems, troubleshoot servers, deploy applications, or stop and restart services. Bolt can be installed on your local workstation and connects directly to remote targets with SSH or WinRM, so you are not required to install any agent software.

Using Bolt it is possible to execute commands, scripts puppet tasks and upload files to Windows and Linux systems. 

Bolt uses SSH to connect to Linux systems and winrm to connect to Windows systems.

The Agentless (Bolt) subtype supports task definitions for Bolt and submits the tasks to Bolt passing any definied arguments. 

# Installation

Download and install the Bolt software as directed on either a Windows or Linux server.

Download the **Agentless(Bolt)** Job Sub-type, stop Enterprise Manager, copy the .jar file into the drop-ins directory and restart Enterprise Manager.

The Job Sub-type should be visible when selecting Windows or Unix job types. If this is not visible, restart the Enterprise Manager using 'Run as Administrator' for the initial time.

Create a global property **BOLT_PATH** that contains the full path of the BOLT binary installation directory.

# Instructions

## Supported Tasks
The opconcli program provides the following tasks:

- **command run**: execute a command on a remote Windows or Linux system.
- **script run**: execute a script on a remote Windows or Linux system. The script is downloaded to the remote system, executed and removed.
- **task run**: execute a pupprt task on a remote Windows or Linux system. The task information is downloaded to the remote system, executed and removed. 
- **file upload**: upload a file to a remote Windows or Linux system.

## Using the Agentless (Bolt) Job Sub-type

When using the Agentless Sub-Type select the required task from the Bolt Task drop-down list. Complete the definitions. Once a task is saved, Bolt Task cannot be changed.

### Remote Command
**Remote Command** is used to execute commands remotely on either a Windows or Linux system.

![diagrmrc](/docs/images/remote_command.PNG)

Field Name        | Description
----------------- | -----------
User              | Is the user name that is used to perform the login to the remote Windows or Linux system. 
Password          | Is the password of the user (encrypted tokens should be used to define passwords).
Run As User       | Is the name of a user that the command should be run under.
Sudo Password     | The password to get root access if required (encrypted tokens should be used to define passwords).
Target(s)         | Is the name or ipadr of the target system. It is possible to define multiple systems by comma separating the names. All defined machines must be of the same type.
Windows           | Indicates if the machine types are Windows. If selected winrm protocol will be used for the connection otherwise SSH will be used for the connection.
SSL               | Indicates if the connection requires SSL. For Linux connections this should always be selected.
No Host Key Check | If selected does not check host keys with SSH.
Command           | The command to execute on the target systems.

example **command run** (can be seen by switching Job Sub-type to <None>)
```
command run 'mkdir /home/opcon/testdir' --targets LINUX001,LINUX002  --no-host-key-check  --user opcon --password (##HIDDEN##)
command run 'mkdir c:\temp\testdir' --targets winrm://WINDOWS001,winrm://WINDOWS002 --no-ssl --user opcon --password (##HIDDEN##)
```
### Exec. Script on Remote
**Exec. Script on Remote** is used to execute a script remotely on either a Windows or Linux system. The script is pushed from the system where Bolt is installed. For Windows the only script type supported is PowerShell.

![diagrmes](/docs/images/script_run.PNG)

Keyword           | Description
----------------- | -----------
User              | Is the user name that is used to perform the login to the remote Windows or Linux system. 
Password          | Is the password of the user (encrypted tokens should be used to define passwords).
Run As User       | Is the name of a user that the command should be run under.
Sudo Password     | The password to get root access if required (encrypted tokens should be used to define passwords).
Target(s)         | Is the name or ipadr of the target system. Itis possible to define multiple systems by comma separating the names. All defined machines must be of the same type.
Windows           | Indicates if the machine types are Windows. If selected winrm protocol will be used for the connection otherwise SSH will be used for the connection.
SSL               | Indicates if the connection requires SSL. For Linux connections this should always be selected.
No Host Key Check | If selected does not check host keys with SSH.
Script            | The full path of the script file on the system where Bolt is installed.
Argument          | Are arguments that are passed to the script.  

example **script run** (can be seen by switching Job Sub-type to <None>)
```
task run 'C:\TestData\scripts\display.sh' '5 10' --targets LINUX002 --no-host-key-check --user opcon --password (##HIDDEN##) 

```

### Exec. Task on Remote
**Exec. Task on Remote** is used to execute a puppet task on either a Windows or Linux system. The task must be registered as a Bolt Task.

![diagrmet](/docs/images/task_run.PNG)

Keyword           | Description
----------------- | -----------
User              | Is the user name that is used to perform the login to the remote Windows or Linux system. 
Password          | Is the password of the user (encrypted tokens should be used to define passwords).
Run As User       | Is the name of a user that the command should be run under.
Sudo Password     | The password to get root access if required (encrypted tokens should be used to define passwords).
Target(s)         | Is the name or ipadr of the target system. Itis possible to define multiple systems by comma separating the names. All defined machines must be of the same type.
Windows           | Indicates if the machine types are Windows. If selected winrm protocol will be used for the connection otherwise SSH will be used for the connection.
SSL               | Indicates if the connection requires SSL. For Linux connections this should always be selected.
No Host Key Check | If selected does not check host keys with SSH.
Task              | The task to execute consists of the following format **modulename::task** where modulename is the registered module and task in the name of the task within the module.
Argument          | Is an argument that is passed to the task. the arguments consist of a name=value pair.  

example **task run** (can be seen by switching Job Sub-type to <None>)
```
task run 'smaopconagents::unixagentcmd' 'port=3100' 'command=start' --targets LINUX002 --no-host-key-check --user opcon --password (##HIDDEN##) --run-as root --sudo-password (##HIDDEN##)

```
### Upload File to Remote
**Upload File to Remote** is used to transfer a file from the system where Bolt is installed to the remote system.

![diagrmup](/docs/images/upload_file.PNG)

Keyword           | Type    | Description
----------------- | ------- | -----------
User              | Text    | Is the user name that is used to perform the login to the remote Windows or Linux system. 
Password          | Text    | Is the password of the user (encrypted tokens should be used to define passwords).
Run As User       | Text    | Is the name of a user that the command should be run under.
Sudo Password     | Text    | The password to get root access if required (encrypted tokens should be used to define passwords).
Target(s)         | Text    | is the name or ipadr of the target system. Itis possible to define multiple systems by comma separating the names. All defined machines must be of the same type.
Windows           | Boolean | Indicates if the machine types are Windows. If selected winrm protocol will be used for the connection otherwise SSH will be used for the connection.
SSL               | Boolean | Indicates if the connection requires SSL. For Linux connections this should always be selected.
No Host Key Check | Boolean | If selected does not check host keys with SSH.
Source            | Text    | The full path of the source file on the system where Bolt is installed.
Destination       | Text    | The full path to the destination file.  

example **task run** (can be seen by switching Job Sub-type to <None>)
```
file upload 'C:\Software\LSAM_19.0.7_Ubuntu14.04_64.tar' '/usr/local/lsam_19.0.7/LSAM_19.0.7_Ubuntu14.04_64.tar' --targets LINUX001,LINUX002 --no-host-key-check --user opcon --password (##HIDDEN##)
file upload 'C:\Software\SMA OpCon Windows LSAM install.msi' 'C:\Software\SMA OpCon Windows LSAM install.msi' --targets winrm://WINDOWS1,winrm://WINDOWS2 --no-ssl --user opcon --password (##HIDDEN##)

```
