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
package com.wl4g.devops.dts.codegen.web;

import com.wl4g.components.common.io.FileIOUtils;
import com.wl4g.components.common.web.rest.RespBase;
import com.wl4g.components.core.framework.operator.GenericOperatorAdapter;
import com.wl4g.components.core.web.BaseController;
import com.wl4g.components.data.page.PageModel;
import com.wl4g.devops.dts.codegen.bean.GenTable;
import com.wl4g.devops.dts.codegen.config.CodegenProperties;
import com.wl4g.devops.dts.codegen.engine.context.GeneratedResult;
import com.wl4g.devops.dts.codegen.engine.converter.DbTypeConverter;
import com.wl4g.devops.dts.codegen.engine.converter.DbTypeConverter.DbType;
import com.wl4g.devops.dts.codegen.engine.resolver.TableMetadata;
import com.wl4g.devops.dts.codegen.service.GenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.wl4g.components.common.io.FileIOUtils.readFullyResourceString;
import static com.wl4g.components.common.lang.Assert2.hasTextOf;
import static com.wl4g.components.common.lang.Assert2.notNullOf;

/**
 * {@link GenerateController}
 *
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2020-09-07
 * @since
 */
@RestController
@RequestMapping("/gen/configure")
public class GenerateController extends BaseController {

	@Autowired
	protected GenericOperatorAdapter<DbType, DbTypeConverter> converter;

	@Autowired
	protected CodegenProperties config;

	@Autowired
	protected GenerateService generateService;

	@RequestMapping("loadTables")
	public RespBase<?> loadTables(Long projectId) {
		RespBase<Object> resp = RespBase.create();
		List<TableMetadata> tables = generateService.loadTables(projectId);
		resp.setData(tables);
		return resp;
	}

	@RequestMapping("loadMetadata")
	public RespBase<?> loadMetadata(Long projectId, String tableName) {
		return generateService.loadMetadata(projectId, tableName);
	}

	@RequestMapping(value = "/list")
	public RespBase<?> list(PageModel pm, String tableName, Long projectId) {
		RespBase<Object> resp = RespBase.create();
		resp.setData(generateService.page(pm, tableName, projectId));
		return resp;
	}

	@RequestMapping("save")
	public RespBase<?> save(@RequestBody GenTable genTable) {
		RespBase<Object> resp = RespBase.create();
		generateService.saveGenConfig(genTable);
		return resp;
	}

	@RequestMapping("detail")
	public RespBase<?> detail(Long tableId) {
		return generateService.detail(tableId);
	}

	@RequestMapping("del")
	public RespBase<?> del(Long id) {
		RespBase<Object> resp = RespBase.create();
		generateService.delete(id);
		return resp;
	}

	@RequestMapping("generate")
	public RespBase<?> generate(Long id, HttpServletResponse response) throws IOException {
		RespBase<Object> resp = RespBase.create();

		// Execution generate.
		GeneratedResult result = generateService.generate(id);
		resp.setData(result.getJobId());
		return resp;
	}

	@RequestMapping("download")
	public void download(String jobId, HttpServletResponse response) throws IOException {
		hasTextOf(jobId, "jobId");

		// Make generated job dir.
		File jobDir = config.generateJobDir(jobId);

		// Add generated README
		FileIOUtils.writeFile(new File(jobDir, "GENERATED.md"), GENERATED_README, false);
		FileIOUtils.writeFile(new File(jobDir, "GENERATED_CN.md"), GENERATED_README_CN, false);

		// ZIP download
		writeZip(response, jobDir.getCanonicalPath(), "codegen-".concat(jobId));
	}

	@RequestMapping("setEnable")
	public RespBase<?> setEnable(Long id, String status) {
		RespBase<Object> resp = RespBase.create();
		notNullOf(id, "id");
		generateService.setEnable(id, status);
		return resp;
	}

	@RequestMapping("getAttrTypes")
	public RespBase<?> getAttrTypes(Long projectId) {
		RespBase<Object> resp = RespBase.create();
		resp.setData(generateService.getAttrTypes(projectId));
		return resp;
	}

	@RequestMapping("synchronizeTable")
	public RespBase<?> synchronizeTable(Long id, boolean force) {
		RespBase<Object> resp = RespBase.create();
		generateService.synchronizeTable(id, force);
		return resp;
	}

	/**
	 * Generated watermark readme.
	 */
	public static final String GENERATED_README = readFullyResourceString("generated/GENERATED.md");
	public static final String GENERATED_README_CN = readFullyResourceString("generated/GENERATED_CN.md");

}