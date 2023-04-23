# **CPU-Scheduler**


## **Table Of Content**

- [Description](#description)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contribution)
- [Testing](#testing)
- [Contact](#contact)



## **Description**

CPU-Scheduler is a simple and awesome program that enables you to visualize the processes to be executed using **Gantt charts**. The program provides the following scheduling options that you can use it in processes visualization:
- FCFS (First-Come-First-Serve)
- SJF-Non-Preemptive (Shortest-Job-First-Non-Preemptive)
- SJF-Preemptive (Shortest-Job-First-Preemptive)
- RR (Round-Robin)
- Priority-Non-Preemptive
- Priority-Preemptive

Also you can use static or dynamic visualization as mentioned above.
**The static scheduling** shows the final gantt chart directly. In contrast the **dynamic scheduling** shows the real time scheduling after each unit time in addition of showing the current ready queue

CPU-Scheduler program is built with the following programming language and  libraries: 

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

![Swing](https://img.shields.io/badge/swing-E6007A?style=for-the-badge&logoColor=white)





## **Installation**

> **Note: The program can be installed for only **Windows OS**** 

To install and use the CPU-Scheduler, follow these step-by-step instructions:

1. Press **Release** that appears in the right of the page
2. Press on the executable file **"CPU-Scheduler-1.0.0.msi"** to download it
3. Choose the location that you want to download the program in
    - By default it is downloaded in the following path: ```C:\Program Files\CPU-Scheduler```
4. Open the executable file **"CPU-Scheduler-1.0.0.msi"**
5. Congratulations the program is runnig ^_^


That's it! With these steps, you can download the program.
The next section will tell you how to use it.



## **Usage**

Here are step-by-step instructions for using the CPU-Scheduler program:

1. Double click on the executable file ***"CPU-Scheduler.exe"***
    - the window that appears is splitted into two sections, the left section and the right one
    - The left section is used for the input, in contrast the right one is used for the output gantt chart whether static or dynamic
2. Choose the scheduler that you want to apply
3. Choose number of processes to be executed
4. Enter the arrival time, and the burst time of each process inside the table appears in the bottom left of the left section and **don't forget to press enter in each row**
5. Press **Static schedule** button if you want to see the final output or press **Dynamic schedule** button if you want to see the output step by step in real time.

## **Testing**

![Coverage badge green][coverage-badge-green] 

[coverage-badge-green]: https://img.shields.io/badge/Coverage-100%25-brightgreen.svg

Unit testing, and integration testing, black ,and white box testing are applied upon the program to prevent, and reduce the bugs

### **Unit Testing**

Each scheduler (FCFS, SJF with preemption and non-preemption, roundrobin, and priority with preemption and non-preemption)

### **Integration Testing**

Testing the GUI with many test suites including corner or boundaries test cases to increase test coverage, prevent program crashes or bugs



## **Contribution**
 
Thank you for considering contributing to this project! Please read the **[Contributor Covenant](https://www.contributor-covenant.org/)** before submitting any contributions.

### **Types of Contributions**

We welcome contributions in the form of bug reports, feature requests, code contributions, and documentation improvements.

### **How to Contribute**

1. Fork the repository and create a new branch.
2. Make your changes and ensure that they are well-documented and tested.
3. Submit a pull request to the main repository and include a detailed description of your changes.

### **Code of Conduct**

We expect all contributors to adhere to the **[Contributor Covenant](https://www.contributor-covenant.org/)**. Please be respectful and inclusive in your contributions and interactions with others.

## Contact

Feel free to reach out to us on any of the following emails:

- zeyadmohamedasu@gmail.com
- omar.amonem@outlook.com
- bishoyyosry@gmail.com
