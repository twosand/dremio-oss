/*
 * Copyright (C) 2017-2018 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.plugins.s3.store;

import java.util.List;

import javax.inject.Provider;

import org.apache.hadoop.fs.Path;

import com.dremio.exec.catalog.StoragePluginId;
import com.dremio.exec.catalog.conf.AWSAuthenticationType;
import com.dremio.exec.catalog.conf.DisplayMetadata;
import com.dremio.exec.catalog.conf.NotMetadataImpacting;
import com.dremio.exec.catalog.conf.Property;
import com.dremio.exec.catalog.conf.Secret;
import com.dremio.exec.catalog.conf.SourceType;
import com.dremio.exec.server.SabotContext;
import com.dremio.exec.store.dfs.FileSystemConf;
import com.dremio.exec.store.dfs.SchemaMutability;

import io.protostuff.Tag;

/**
 * Connection Configuration for S3.
 */
@SourceType(value = "S3", label = "Amazon S3")
public class S3PluginConfig extends FileSystemConf<S3PluginConfig, S3StoragePlugin> {

  //  optional string access_key = 1;
  //  optional string access_secret = 2;
  //  optional bool secure = 3;
  //  repeated string external_bucket = 4;
  //  repeated Property property = 5;
  //  optional bool allow_create_drop = 6;
  //  optional string root_path = 7;
  //  optional AWSAuthenticationType credential_type = 8;

  @Tag(1)
  @DisplayMetadata(label = "AWS Access Key")
  public String accessKey = "";

  @Tag(2)
  @Secret
  @DisplayMetadata(label = "AWS Access Secret")
  public String accessSecret = "";

  @Tag(3)
  @DisplayMetadata(label = "Encrypt connection")
  public boolean secure;

  @Tag(4)
  @DisplayMetadata(label = "External Buckets")
  public List<String> externalBucketList;

  @Tag(5)
  public List<Property> propertyList;

  @Tag(6)
  @NotMetadataImpacting
  @DisplayMetadata(label = "Enable exports into the source (CTAS and DROP)")
  public boolean allowCreateDrop;

  @Tag(7)
  @DisplayMetadata(label = "Root Path")
  public String rootPath = "/";

  @Tag(8)
  public AWSAuthenticationType credentialType = AWSAuthenticationType.ACCESS_KEY;

  @Override
  public S3StoragePlugin newPlugin(SabotContext context, String name, Provider<StoragePluginId> pluginIdProvider) {
    return new S3StoragePlugin(this, context, name, pluginIdProvider);
  }

  @Override
  public Path getPath() {
    return new Path(rootPath);
  }

  @Override
  public boolean isImpersonationEnabled() {
    return false;
  }

  @Override
  public String getConnection() {
    return "dremioS3:///";
  }

  @Override
  public SchemaMutability getSchemaMutability() {
    return allowCreateDrop ? SchemaMutability.USER_TABLE : SchemaMutability.NONE;
  }

  @Override
  public List<Property> getProperties() {
    return propertyList;
  }
}
