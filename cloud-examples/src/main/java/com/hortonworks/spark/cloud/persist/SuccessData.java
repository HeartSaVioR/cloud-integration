/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hortonworks.spark.cloud.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.s3a.commit.ValidationFailure;

/**
 * Summary data saved into a {@code _SUCCESS} marker file.
 *
 * This provides an easy way to determine which committer was used
 * to commit work.
 * <ol>
 *   <li>File length == 0: classic {@code FileOutputCommitter}.</li>
 *   <li>Loadable as {@link SuccessData}:
 *   A s3guard committer with name in in {@link #committer} field.</li>
 *   <li>Not loadable? Something else.</li>
 * </ol>
 *
 * This is an unstable structure intended for diagnostics and testing.
 */
@InterfaceAudience.Private
@InterfaceStability.Unstable
public class SuccessData extends PersistentCommitData {
  private static final Logger LOG = LoggerFactory.getLogger(SuccessData.class);

  /**
   * Serialization ID: {@value}.
   */
  private static final long serialVersionUID = 507133045258460084L;

  /** Timestamp of creation. */
  private long timestamp;

  /** Timestamp as date; no expectation of parseability. */
  private String date;

  /**
   * Host which created the file (implicitly: committed the work).
   */
  private String hostname;

  /**
   * Committer name.
   */
  private String committer;

  /**
   * Description text.
   */
  private String description;

  /**
   * Metrics.
   */
  private Map<String, Long> metrics = new HashMap<>();

  /**
   * Filenames in the commit.
   */
  private List<String> filenames = new ArrayList<>(0);

  @Override
  public void validate() throws ValidationFailure {

  }

  @Override
  public byte[] toBytes() throws IOException {
    return serializer().toBytes(this);
  }

  @Override
  public void save(FileSystem fs, Path path, boolean overwrite)
      throws IOException {
    serializer().save(fs, path, this, overwrite);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(
        "SuccessData{");
    sb.append("committer='").append(committer).append('\'');
    sb.append(", hostname='").append(hostname).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", date='").append(date).append('\'');
    sb.append(", filenames=[").append(
        StringUtils.join(filenames, ", "))
        .append("]");
    sb.append('}');
    return sb.toString();
  }

  /**
   * Dump the metrics (if any) to a string.
   * The metrics are sorted for ease of viewing.
   * @param prefix prefix before every entry
   * @param middle string between key and value
   * @param suffix suffix to each entry
   * @return the dumped string
   */
  public String dumpMetrics(String prefix, String middle, String suffix) {
    if (metrics == null) {
      return "";
    }
    List<String> list = new ArrayList<>(metrics.keySet());
    Collections.sort(list);
    StringBuilder sb = new StringBuilder(list.size() * 32);
    for (String k : list) {
      sb.append(prefix)
          .append(k)
          .append(middle)
          .append(metrics.get(k))
          .append(suffix);
    }
    return sb.toString();
  }

  /**
   * Load an instance from a file, then validate it.
   * @param fs filesystem
   * @param path path
   * @return the loaded instance
   * @throws IOException IO failure
   * @throws ValidationFailure if the data is invalid
   */
  public static SuccessData load(FileSystem fs, Path path)
      throws IOException {
    LOG.debug("Reading success data from {}", path);
    SuccessData instance = serializer().load(fs, path);
    instance.validate();
    return instance;
  }

  /**
   * Get a JSON serializer for this class.
   * @return a serializer.
   */
  private static JsonSerialization<SuccessData> serializer() {
    return new JsonSerialization<>(SuccessData.class, false, true);
  }

  /** @return timestamp of creation. */
  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /** @return timestamp as date; no expectation of parseability. */
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  /**
   * @return host which created the file (implicitly: committed the work).
   */
  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  /**
   * @return committer name.
   */
  public String getCommitter() {
    return committer;
  }

  public void setCommitter(String committer) {
    this.committer = committer;
  }

  /**
   * @return any description text.
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return any metrics.
   */
  public Map<String, Long> getMetrics() {
    return metrics;
  }

  public void setMetrics(Map<String, Long> metrics) {
    this.metrics = metrics;
  }

  /**
   * @return a list of filenames in the commit.
   */
  public List<String> getFilenames() {
    return filenames;
  }

  public void setFilenames(List<String> filenames) {
    this.filenames = filenames;
  }
}
