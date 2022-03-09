# **Contributing Guide**
This is Beagle's contributing guide. You will find here some resources to help you contribute.

## **Table of contents**
### 1. [**Before you contribute**](#before-you-contribute)
> #### 1.1. [**Code of Conduct**](#code-of-conduct)
> #### 1.2. [**Legal**](#legal)
### 2. [**Prerequisites**](#prerequisites)
>#### 2.1. [**Pull Request title**](#pull-request-title)
> #### 2.2. [**Issues**](#issues)
> ##### 2.2.1. [**Check the issue tracker**](#check-the-issue-tracker)
> ##### 2.2.2. [**Open an issue for any new problem**](#open-an-issue-for-any-new-problem)
> #### 2.3. [**Developer Certificate of Origin - DCO**](#developer-certificate-of-origin---dco)
### 3. [**How to contribute?**](#how-to-contribute?)
>#### 3.1. [**How can you contribute?**](#how-can-you-contribute?)
>#### 3.2. [**Contribution Guideline**](#contribution-guideline)
>#### 3.3 [**Pull Request's approval**](#pull-request's-approval)
### 4. [**Community**](#community)


## **Before you contribute**

### **Code of Conduct**
Please follow the [**Code of Conduct**](https://github.com/ZupIT/beagle/blob/main/CODE_OF_CONDUCT.md) in all your interactions with our project.

### **Legal**
- Beagle is licensed over [**ASF - Apache License**](https://github.com/ZupIT/charlescd/blob/main/LICENSE), version 2, so new files must have the ASL version 2 header. For more information, please check out [**Apache license**]( https://www.apache.org/licenses/LICENSE-2.0).

- All contributions are subject to the [**Developer Certificate of Origin (DCO)**](https://developercertificate.org). 
When you commit, use the ```**-s** ``` option to include the Signed-off-by line at the end of the commit log message.

## **Prerequisites**
Check out the requisites before contributing to Beagle:

### **Pull Request title**
When opening a PR, add the title with a prefix of the type of change you are contributing, for example `feat:` for a new feature. You can see other types in the [**What can you contribute?**](#What-can-you-contribute?) section.

Your Pull Request should look like this:

```txt
feat: add field in component image to allow resizing
```
This format is called **conventional commits**. It helps us understand what each modification is actually doing, and allow us to automatically generate better *release notes* and *versions*.

For more information, please read the [**official specifications**](https://www.conventionalcommits.org/).

### **Issues**

If you have a bug or an idea, check out the following sections before submitting your contribution.

#### **Check the issue tracker**

All our issues are centralized in our [**main repository**](https://github.com/ZupIT/beagle), it is quite likely that you will find a topic that is being discussed. Check the [**open issues**](https://github.com/ZupIT/beagle/issues), another good way to start is [**good first issues**](https://github.com/ZupIT/beagle/issues?q=is%3Aissue+label%3A%22good+first+issue%22+is%3Aopen).

#### **Open an issue for any new problem**

Writing a good issue will help our team better analyze and manage your contributions, therefore, follow the standards and best practices below:

**With the title:**

**Project:Scope - Title Description**  

- **Project:** Name of the project or repository you want to contribute to.  

- **Scope:** Add what your issue refers to:

  - **[Bug report](https://github.com/ZupIT/beagle/issues/new?assignees=&labels=bug&template=bug_report.md&title=):** Report a reproducible bug.

  - **[Feature request](https://github.com/ZupIT/beagle/issues/new?assignees=&labels=&template=feature_request.md&title=):** Suggest a new idea for Beagle.

> **Example: Beagle:feat request - Suggestion for a better user experience** 

**With the issue description:**

Try to explain the scenario to us by following these tips:

 - **Context:** explain the conditions which led you to write this issue.
 - **Problem or idea:** the context should lead to something, an idea or a problem that you’re facing.
 - **Solution or next step:** this where you move forward. You can engage others (request feedback), assign somebody else to the issue, or simply leave it for further investigation, but you absolutely need to propose a next step towards solving the issue.

### **Developer Certificate of Origin - DCO**

This is a security layer for the project and for the developers. It is mandatory.
 
 Follow one of these two methods to add DCO to your commits:
 
**1. Command line**
 Follow the steps: 
 **Step 1:** Configure your local git environment adding the same name and e-mail configured at your GitHub account. It helps to sign commits manually during reviews and suggestions.

 ```
git config --global user.name “Name”
git config --global user.email “email@domain.com.br”
```
**Step 2:** Add the Signed-off-by line with the `'-s'` flag in the git commit command:

```
$ git commit -s -m "This is my commit message"
```

**2. GitHub website**
You can also manually sign your commits during GitHub reviews and suggestions, follow the steps below: 

**Step 1:** When the commit changes box opens, manually type or paste your signature in the comment box, see the example:

```
Signed-off-by: Name < e-mail address >
```

For this method, your name and e-mail must be the same registered on your GitHub account.

## **How to contribute?** 
See the guidelines to submit your changes. 

### **How can you contribute?**
You can contibute with a new feature, bug fix, documentation and more, check out below:

- **feat:** A new feature (adding a new component, providing new variants for an existing component, etc.).
- **fix:** A bug fix (correcting a styling issue, addressing a bug in a component's API, etc.).
  When updating non-dev dependencies, mark your changes with the `fix:` type.
- **docs:** Documentation-only changes.
- **style:** Changes that do not affect the meaning of the code
(whitespace, formatting, missing semicolons, etc). _Not_ to be used for CSS changes as those are meaningful changes, consider using `feat:` of `fix:` instead.
- **refactor:** A code change that neither fixes a bug nor adds a feature.
- **perf:** A code change that improves performance.
- **test:** Adding missing tests or correcting existing tests.
- **build:** Changes that affect the build system (changing webpack or Rollup config for example).
- **ci:** Changes to our CI configuration files and scripts (changing `Fastlane`, adding or changing Danger plugins, etc.).
- **chore:** Other changes that don't modify source or test files. Use this type when adding or updating dev dependencies.
- **revert:** Reverts a previous commit.

> **Note:**
> If you're introducing a breaking change, the message body should start with [**`BREAKING CHANGE:`**](https://www.conventionalcommits.org/en/v1.0.0/#commit-message-with-description-and-breaking-change-footer).

### **Contribution Guideline**
Follow the Pull Request submission guidelines below: 

**Step 1.** Search for issues and PRs that relate to your submission to avoid duplication;

**Step 2.** Make sure that the issue describes the problem you're fixing, or documents the design for the feature you'd like to add. Discussing the design upfront helps to ensure that we're ready to accept your work.

**Step 3.** Fork the ZupIT/beagle repo;

**Step 4.** Make your changes in a new git branch.

**Step 5.** Follow our [**Coding Rules**](https://github.com/ZupIT/beagle/blob/main/doc/contributing/coding_rules.md);

**Step 6.** Commit your changes using a descriptive commit message, [**Signing all your commits**](https://github.com/ZupIT/beagle/blob/main/doc/contributing/dco_rules.md) and making sure to **include appropriate test cases**.=;

**Step 7.** Push your branch to GitHub;

**Step 8.** In GitHub, send a pull request to **`beagle:main`**;

### **Pull Request's approval**
After Beagle's team analyze your contribution, we may add some questions and suggestions, you should:

- Make the required updates.
- Re-run Beagle's test suites and CI workflows to ensure everything is still working properly.
- Rebase your branch and force push to your GitHub repository (this will update your Pull Request):

  ```shell
  git rebase main -i
  git push -f
  ```

## **Community**

- Do you have any question about Beagle? Let's chat in our [**forum**](https://forum.zup.com.br/).


**Thank you for your contribution!**

**Beagle Team** 😁
