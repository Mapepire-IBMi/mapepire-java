# Contributing to Mapepire Java Client SDK

Thank you for considering contributing to the Mapepire Java Client SDK! We appreciate your support and are excited to collaborate with you. This document outlines the process for contributing to the project.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
  - [Reporting Bugs](#reporting-bugs)
  - [Suggesting Enhancements](#suggesting-enhancements)
  - [Submitting Changes](#submitting-changes)
- [Development Workflow](#development-workflow)
  - [Getting Started](#getting-started)
  - [Creating a Branch](#creating-a-branch)
  - [Making Changes](#making-changes)
  - [Committing Changes](#committing-changes)
  - [Submitting a Pull Request](#submitting-a-pull-request)
- [Style Guide](#style-guide)
- [License](#license)
- [Acknowledgements](#acknowledgements)

## Code of Conduct

By participating in this project, you agree to abide by our [Code of Conduct](CODE_OF_CONDUCT.md). Please read it to understand the expectations for our community.

## How Can I Contribute?

### Reporting Bugs

If you find a bug, please report it by opening an issue on our [GitHub Issues page](https://github.com/your-username/mapepire-java/issues). Provide as much detail as possible, including steps to reproduce the issue, your environment, and any relevant logs.

Before you start working on a bug, please comment on the issue to let us know that you are working on it. This helps prevent duplicate efforts.

### Suggesting Enhancements

We welcome new ideas! If you have a feature request or enhancement suggestion, please open an issue on our [GitHub Issues page](https://github.com/your-username/mapepire-java/issues) and describe the enhancement you'd like to see. 

### Submitting Changes

If you would like to contribute code to the project, follow the steps below to get started.

## Development Workflow

### Getting Started

1. Fork the repository to your own GitHub account.
2. Clone your forked repository to your local machine:

    ```sh
    git clone https://github.com/your-username/mapepire-java.git
    cd mapepire-java
    ```

3. Install the necessary dependencies:

    ```sh
    ./gradlew build
    ```

### Creating a Branch

Create a new branch for your changes. Use a descriptive name for the branch to reflect the work being done.

## Making Changes
Make your changes to the codebase. Ensure that your changes adhere to our [style guide](#style-guide).

## Committing Changes
Commit your changes with a descriptive commit message. Follow the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) specification for your commit messages.


git add .
git commit -m "feat: add new feature description"
# Submitting a Pull Request

1. Push your changes to your forked repository:

    ```sh
    git push origin feature/your-feature-name
    ```

2. Open a pull request on the original repository. Provide a detailed description of your changes, including the motivation and context for the change.

3. Link any related issues in the pull request description.

4. Ensure that all status checks pass (e.g., CI tests).

5. Wait for a project maintainer to review and approve your pull request. Be responsive to feedback and make any requested changes.

## Style Guide

- Follow the Google Java Style Guide for code formatting.
- Ensure your code is well-documented with Javadoc comments.
- Write unit tests for new features and bug fixes.

## LICENSE

By contributing to this project, you agree that your contributions will be licensed under the following license. [LICENSE](LICENSE)

## Acknowledgements

We sincerely thank all our contributors for their time and effort. Whether you contribute code, report issues, or suggest enhancements, your help is invaluable. Every bit of help, big or small, makes a significant difference. Thank you!
