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

package com.hortonworks.spark.cloud.adl

import java.net.URI

import com.hortonworks.spark.cloud.common.CloudTestKeys._
import com.hortonworks.spark.cloud.common.CopyCsvFileTrait
import org.apache.hadoop.fs.FileSystem

/**
 * Trait for ADL tests.
 *
 * This trait supports CSV data source by copying over the data from S3A if
 * it isn't already in a ADL URL
 */
trait AdlTestSetup extends CopyCsvFileTrait {

  override def enabled: Boolean =  {
    getConf.getBoolean(ADL_TESTS_ENABLED, false)
  }

  def initFS(): FileSystem = {
    val uri = new URI(requiredOption(ADL_TEST_URI))
    logDebug(s"Executing Azure tests against $uri")
    createFilesystem(uri)
  }

}
