/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wl4g.devops.dts.codegen.service;

import com.wl4g.components.common.web.rest.RespBase;
import com.wl4g.components.data.page.PageModel;
import com.wl4g.devops.dts.codegen.bean.GenTable;
import com.wl4g.devops.dts.codegen.engine.context.GeneratedResult;
import com.wl4g.devops.dts.codegen.engine.resolver.TableMetadata;

import java.util.List;
import java.util.Set;

/**
 * {@link GenerateService}
 *
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020-09-07
 * @since
 */
public interface GenerateService {

	List<TableMetadata> loadTables(Long projectId);

	/**
	 * new gen code
	 * 
	 * @param databaseId
	 * @param tableName
	 */
	RespBase<Object> loadMetadata(Long projectId, String tableName);

	PageModel page(PageModel pm, String tableName, Long projectId);

	RespBase<Object> detail(Long tableId);

	void saveGenConfig(GenTable genTable);

	void delete(Long tableId);

	GeneratedResult generate(Long tableId);

	Set<String> getAttrTypes(Long projectId);

	void setEnable(Long id, String status);

	void synchronizeTable(Long id, boolean focus);
}