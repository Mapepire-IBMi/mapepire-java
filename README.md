# Mapepire Java Client SDK

[![Maven Central](https://img.shields.io/maven-central/v/io.github.mapepire-ibmi/mapepire-sdk.svg?label=Maven%20Central&logo=apachemaven)](https://central.sonatype.com/artifact/io.github.mapepire-ibmi/mapepire-sdk/)
[![Maven Build](https://github.com/Mapepire-IBMi/mapepire-java/actions/workflows/build.yml/badge.svg)](https://github.com/Mapepire-IBMi/mapepire-java/actions/workflows/build.yml)
[![License](https://img.shields.io/github/license/allenai/tango.svg?color=blue&cachedrop)](https://github.com/Mapepire-IBMi/mapepire-java/blob/main/LICENSE)

## Overview

`mapepire-java` is a Java client SDK that leverages the [`mapepire-server`](https://github.com/Mapepire-IBMi/mapepire-server) to provide a new and convenient way to access Db2 on IBM i.

Full Documentation: https://mapepire-ibmi.github.io

## Setup

### Requirements

* Java 8 or later

### Install with `maven`

```xml
<dependency>
    <groupId>io.github.mapepire-ibmi</groupId>
    <artifactId>mapepire-sdk</artifactId>
    <version>0.0.6</version>  <!-- Use the latest version -->
</dependency>
```

### Server Component Setup

In order for applications to use Db2 for i through this Java client SDK, the `mapepire-server` daemon must be installed and started-up on each IBM i. Follow the instructions [here](https://mapepire-ibmi.github.io/guides/sysadmin/) to learn about the installation and startup process of the server component.

## Example Usage

The following Java program initializes a `DaemonServer` object that will be used to connect with the Server Component. A single `SqlJob` object is created to facilitate this connection from the client side. A `query` object is then initialized with a simple `SELECT` query which is finally executed with 3 rows being fetched and the results being printed.

> [!NOTE]
> To run this Java program, replace the credentials which are passed into the `DaemonServer` object.

```java
package io.github.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.github.mapepire_ibmi.Query;
import io.github.mapepire_ibmi.SqlJob;
import io.github.mapepire_ibmi.types.DaemonServer;
import io.github.mapepire_ibmi.types.QueryResult;

public final class App {
    public static void main(String[] args) throws Exception {
        // Initialize credentials
        DaemonServer creds = new DaemonServer("HOST", 8085, "USER", "PASSWORD", true, "CA");

        // Establish connection
        SqlJob job = new SqlJob();
        job.connect(creds).get();

        // Initialize and execute query
        Query query = job.query("SELECT * FROM SAMPLE.DEPARTMENT");
        QueryResult<Object> result = query.execute(3).get();

        // Convert to JSON string and output
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = mapper.writeValueAsString(result);
        System.out.println(jsonString);
    }
}
```

Output:

```json
{
  "id" : "query3",
  "success" : true,
  "error" : null,
  "sql_rc" : 0,
  "sql_state" : null,
  "metadata" : {
    "column_count" : 5,
    "columns" : [ {
      "display_size" : 3,
      "label" : "DEPTNO",
      "name" : "DEPTNO",
      "type" : "CHAR"
    }, {
      "display_size" : 36,
      "label" : "DEPTNAME",
      "name" : "DEPTNAME",
      "type" : "VARCHAR"
    }, {
      "display_size" : 6,
      "label" : "MGRNO",
      "name" : "MGRNO",
      "type" : "CHAR"
    }, {
      "display_size" : 3,
      "label" : "ADMRDEPT",
      "name" : "ADMRDEPT",
      "type" : "CHAR"
    }, {
      "display_size" : 16,
      "label" : "LOCATION",
      "name" : "LOCATION",
      "type" : "CHAR"
    } ],
    "job" : "930740/QUSER/QZDASOINIT"
  },
  "is_done" : false,
  "has_results" : true,
  "update_count" : -1,
  "data" : [ {
    "DEPTNO" : "A00",
    "DEPTNAME" : "SPIFFY COMPUTER SERVICE DIV.",
    "MGRNO" : "000010",
    "ADMRDEPT" : "A00",
    "LOCATION" : null
  }, {
    "DEPTNO" : "B01",
    "DEPTNAME" : "PLANNING",
    "MGRNO" : "000020",
    "ADMRDEPT" : "A00",
    "LOCATION" : null
  }, {
    "DEPTNO" : "C01",
    "DEPTNAME" : "INFORMATION CENTER",
    "MGRNO" : "000030",
    "ADMRDEPT" : "A00",
    "LOCATION" : null
  } ]
}
```

## Sample Projects

Check out all the [Java Sample Projects](https://github.com/Mapepire-IBMi/samples/tree/main/java) that showcase how this client SDK can be used in various applications.
