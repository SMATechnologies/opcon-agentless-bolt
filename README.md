# OpCon Agent-less via Bolt
Provides a Job subtype to drive agentless workload on remote Windows and Linux systems using [Puppet Bolt](https://puppet.com/open-source/bolt/).

Bolt is an open source orchestration tool that automates the manual work it takes to maintain your infrastructure. Use Bolt to automate tasks that you perform on an as-needed basis or as part of a greater orchestration workflow. For example, you can use Bolt to patch and update systems, troubleshoot servers, deploy applications, or stop and restart services. Bolt can be installed on your local workstation and connects directly to remote targets with SSH or WinRM, so you are not required to install any agent software.

![diagrm](/docs/images/Connectors_overview.png)

Using Bolt it is possible to execute commands, scripts puppet tasks and upload files to Windows and Linux systems. 

Bolt uses SSH to connect to Linux systems and winrm to connect to Windows systems.

The Agentless (Bolt) subtype supports task definitions for Bolt and submits the tasks to Bolt passing any definied arguments. 

# Disclaimer
No Support and No Warranty are provided by SMA Technologies for this project and related material. The use of this project's files is on your own risk.

SMA Technologies assumes no liability for damage caused by the usage of any of the files offered here via this Github repository.

# Prerequisites
Requires Bolt version 2.x or greater : https://puppet.com/open-source/bolt/.

The Bolt software should be installed on either a Windows or Linux server with the appropriate OpCon Agent.

Agentless (Bolt) Job Sub-Type requires OpCon 17 or greater. 

# Instructions
Install the Bolt software as directed.

Download the AgentlessBolt job sub-type, stop Enterprise Manager, copy the jar file into the drop-ins directory and restart Enterprise Manager.

The job sub-type should be visible when selecting Windows or Unix job types.

For usage instructions see the opcon-agentless-bolt.md documentation

## Supported Tasks
The opconcli program provides the following tasks:

- **command run**: execute a command on a remote Windows or Linux system.
- **script run**: execute a script on a remote Windows or Linux system. The script is downloaded to the remote system, executed and removed.
- **task run**: execute a pupprt task on a remote Windows or Linux system. The task information is downloaded to the remote system, executed and removed. 
- **file upload**: upload a file to a remote Windows or Linux system.

Using the Agentless (Bolt) Job Sub-Type.

When using the Agentless Sub-Type select the required task from the Bolt Task drop-down list. Once a job is saved, the job type cannot be changed.

# License
Copyright 2019 SMA Technologies

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at [apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

# Contributing
We love contributions, please read our [Contribution Guide](CONTRIBUTING.md) to get started!

# Code of Conduct
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-v2.0%20adopted-ff69b4.svg)](code-of-conduct.md)
SMA Technologies has adopted the [Contributor Covenant](CODE_OF_CONDUCT.md) as its Code of Conduct, and we expect project participants to adhere to it. Please read the [full text](CODE_OF_CONDUCT.md) so that you can understand what actions will and will not be tolerated.
