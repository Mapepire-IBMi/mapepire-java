# mapepire-java

[![Maven Build](https://github.com/Mapepire-IBMi/mapepire-java/actions/workflows/build.yml/badge.svg)](https://github.com/Mapepire-IBMi/mapepire-java/actions/workflows/build.yml) [![License](https://img.shields.io/github/license/allenai/tango.svg?color=blue&cachedrop)](https://github.com/Mapepire-IBMi/mapepire-java/blob/main/LICENSE)

## Overview

`mapepire-java` is a Java client SDK that leverages the [`mapepire-server`](https://github.com/Mapepire-IBMi/mapepire-server) to provide a new and convenient way to access Db2 on IBM i.

Full Documentation: https://mapepire-ibmi.github.io

## Setup

### Requirements

* Java 8 or later

### Install with `maven` (Forthcoming)

```xml
<dependency>
    <groupId>io.github.mapapire</groupId>
    <artifactId>mapapire-java</artifactId>
    <version>x.x.x</version>
</dependency>
```

### Server Component Setup

In order for applications to use Db2 for i through this Java client SDK, the `mapepire-server` daemon must be installed and started-up on each IBM i. Follow the instructions [here](https://mapepire-ibmi.github.io/guides/sysadmin/) to learn about the installation and startup process of the server component.

## Example Usage

The following Java program creates a `DaemonServer` object that will be used to connect with the Server Component. Then a single `SqlJob` is created to facilitate the connection from the client side.

```java

```

Output:

```json
{
  "id":"query3",
  "has_results":true,
  "update_count":-1,
  "metadata":{
    "column_count":14,
    "job":"330955/QUSER/QZDASOINIT",
    "columns":[
      {
        "name":"EMPNO",
        "type":"CHAR",
        "display_size":6,
        "label":"EMPNO"
      },
      {
        "name":"FIRSTNME",
        "type":"VARCHAR",
        "display_size":12,
        "label":"FIRSTNME"
      },
      {
        "name":"MIDINIT",
        "type":"CHAR",
        "display_size":1,
        "label":"MIDINIT"
      },
      {
        "name":"LASTNAME",
        "type":"VARCHAR",
        "display_size":15,
        "label":"LASTNAME"
      },
      {
        "name":"WORKDEPT",
        "type":"CHAR",
        "display_size":3,
        "label":"WORKDEPT"
      },
      {
        "name":"PHONENO",
        "type":"CHAR",
        "display_size":4,
        "label":"PHONENO"
      },
      {
        "name":"HIREDATE",
        "type":"DATE",
        "display_size":10,
        "label":"HIREDATE"
      },
      {
        "name":"JOB",
        "type":"CHAR",
        "display_size":8,
        "label":"JOB"
      },
      {
        "name":"EDLEVEL",
        "type":"SMALLINT",
        "display_size":6,
        "label":"EDLEVEL"
      },
      {
        "name":"SEX",
        "type":"CHAR",
        "display_size":1,
        "label":"SEX"
      },
      {
        "name":"BIRTHDATE",
        "type":"DATE",
        "display_size":10,
        "label":"BIRTHDATE"
      },
      {
        "name":"SALARY",
        "type":"DECIMAL",
        "display_size":11,
        "label":"SALARY"
      },
      {
        "name":"BONUS",
        "type":"DECIMAL",
        "display_size":11,
        "label":"BONUS"
      },
      {
        "name":"COMM",
        "type":"DECIMAL",
        "display_size":11,
        "label":"COMM"
      }
    ]
  },
  "data":[
    {
      "EMPNO":"000010",
      "FIRSTNME":"CHRISTINE",
      "MIDINIT":"I",
      "LASTNAME":"HAAS",
      "WORKDEPT":"A00",
      "PHONENO":"3978",
      "HIREDATE":"01/01/65",
      "JOB":"PRES",
      "EDLEVEL":18,
      "SEX":"F",
      "BIRTHDATE":"None",
      "SALARY":52750.0,
      "BONUS":1000.0,
      "COMM":4220.0
    }
  ],
  "is_done":false,
  "success":true
}
```