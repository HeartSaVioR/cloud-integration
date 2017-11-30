/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hortonworks.spark.cloud.integration

import com.hortonworks.spark.cloud.utils.HConf
import org.apache.hadoop.conf.Configuration
import org.scalatest.FunSuite

import org.apache.spark.SparkConf

class GeneratorSuite extends FunSuite with HConf {


  test("create generator") {
    val conf = new SparkConf()
    val generator = new Generator()
    assert(-2 === generator.action(conf, List[String]()))
  }


  test("dump ASL constants") {
    val conf = new Configuration()
    dumpConfigOptions(conf, "fs.adl")
  }

  test("dump s3a constants") {
    val conf = new Configuration()
    dumpConfigOptions(conf, "fs.s3a")
  }
}
