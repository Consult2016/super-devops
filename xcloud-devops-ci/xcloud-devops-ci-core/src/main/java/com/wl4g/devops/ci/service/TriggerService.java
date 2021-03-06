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
package com.wl4g.devops.ci.service;

import com.wl4g.components.core.bean.ci.Trigger;
import com.wl4g.devops.page.PageModel;

/**
 * @author vjay
 * @date 2019-05-17 11:04:00
 */
public interface TriggerService {

	PageModel list(PageModel pm, Long id, String name, Long taskId, Integer enable, String startDate, String endDate);

	void save(Trigger trigger);

	int delete(Long id);

	void enable(Long id);

	void disable(Long id);

	void updateSha(Long id, String sha);

	Trigger getById(Long id);

}