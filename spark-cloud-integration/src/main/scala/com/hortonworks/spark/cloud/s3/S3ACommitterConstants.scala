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

package com.hortonworks.spark.cloud.s3

import com.hortonworks.spark.cloud.commit.CommitterConstants

/**
 * Constants related to the S3A committers.
 * Originally a copy & paste of the java values, it's now just a reference,
 * though retained to reserve the option of moving back to copied values.
 */
object S3ACommitterConstants {

  val S3A_COMMITTER_KEY = String.format(
    CommitterConstants.OUTPUTCOMMITTER_FACTORY_SCHEME_PATTERN,
    "s3a")
  val STAGING_PACKAGE = "org.apache.hadoop.fs.s3a.commit.staging."
    STAGING_PACKAGE + "DirectoryStagingCommitterFactory"
  val DEFAULT_CREATE_SUCCESSFUL_JOB_DIR_MARKER = true
  val S3A_COMMITTER_FACTORY_KEY: String = String
    .format("mapreduce.outputcommitter.factory.scheme.%s", "s3a")
  val STAGING_COMMITTER_FACTORY = "org.apache.hadoop.fs.s3a.commit.staging.StagingCommitterFactory"
  val DIRECTORY_COMMITTER_FACTORY = "org.apache.hadoop.fs.s3a.commit.staging.DirectoryStagingCommitterFactory"
  val PARTITIONED_COMMITTER_FACTORY = "org.apache.hadoop.fs.s3a.commit.staging.PartitonedStagingCommitterFactory"
  val DYNAMIC_COMMITTER_FACTORY = "org.apache.hadoop.fs.s3a.commit.DynamicCommitterFactory"
  val MAGIC_COMMITTER_FACTORY = "org.apache.hadoop.fs.s3a.commit.magic.MagicS3GuardCommitterFactory"
  val FS_S3A_COMMITTER_NAME = "fs.s3a.committer.name"
  val COMMITTER_NAME_FILE = "file"

  val MAGIC = "magic"
  val STAGING = "staging"
  val DYNAMIC = "dynamic"
  val DIRECTORY = "directory"
  val PARTITIONED = "partitioned"
  val DEFAULT_RENAME = "default"

  /**
   * Committer name to: name in _SUCCESS, factory classname, requires consistent FS.
   *
   * If the first field is "", it means "this committer doesn't put its name into
   * the success file (or that it isn't actually created).
   */
  val COMMITTERS_BY_NAME: Map[String, (String, String, Boolean)] = Map(
    MAGIC -> ("MagicS3GuardCommitter",  MAGIC_COMMITTER_FACTORY, true),
    STAGING -> ("StagingCommitter",  STAGING_COMMITTER_FACTORY, false),
    DYNAMIC -> ("",  DYNAMIC_COMMITTER_FACTORY, false),
    DIRECTORY -> ("DirectoryStagingCommitter",  DIRECTORY_COMMITTER_FACTORY, false),
    PARTITIONED -> ("PartitionedStagingCommitter",  PARTITIONED_COMMITTER_FACTORY, false),
    DEFAULT_RENAME -> ("", CommitterConstants.DEFAULT_COMMITTER_FACTORY, true)
  )
}
