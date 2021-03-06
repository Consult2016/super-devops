// ${watermark}

${javaSpecs.escapeCopyright(copyright)}

<#assign aDateTime = .now>
<#assign aDate = aDateTime?date>
<#--package name-->
package ${packageName}.${serviceSubModulePackageName}.impl;

<#--import-->
import static com.wl4g.components.common.lang.Assert2.notNullOf;
import com.wl4g.components.data.page.PageModel;
import com.github.pagehelper.PageHelper;
import com.wl4g.components.core.bean.BaseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${organType}.${organName}.${projectName}.common.${moduleName}.${beanSubModulePackageName}.${entityName?cap_first};
import ${packageName}.${daoSubModulePackageName}.${entityName?cap_first}Dao;
import ${packageName}.${serviceSubModulePackageName}.${entityName?cap_first}Service;

import static java.util.Objects.isNull;

/**
 * ${comments} service implements of {@link ${entityName?cap_first}}
 *
 * @author ${author}
 * @version ${version}
 * @Date ${now}
 * @since ${since}
 */
@Service
public class ${entityName}ServiceImpl implements ${entityName}Service {

    @Autowired
    private ${entityName}Dao ${entityName?uncap_first}Dao;

    @Override
    public PageModel page(PageModel pm, ${entityName} ${entityName?uncap_first}) {
        pm.page(PageHelper.startPage(pm.getPageNum(), pm.getPageSize(), true));
        pm.setRecords(${entityName?uncap_first}Dao.list(${entityName?uncap_first}));
        return pm;
    }

    @Override
    public int save(${entityName} ${entityName?uncap_first}) {
        if (isNull(${entityName?uncap_first}.getId())) {
        	${entityName?uncap_first}.preInsert();
            return ${entityName?uncap_first}Dao.insertSelective(${entityName?uncap_first});
        } else {
        	${entityName?uncap_first}.preUpdate();
            return ${entityName?uncap_first}Dao.updateByPrimaryKeySelective(${entityName?uncap_first});
        }
    }

    @Override
    public ${entityName} detail(Long id) {
        notNullOf(id, "${entityName?uncap_first}Id");
        return ${entityName?uncap_first}Dao.selectByPrimaryKey(id);
    }

<#-- Service delete with logical  -->
<#if optionMap.tableDeleteType == 'deleteWithLogical'>
    @Override
    public int del(Long id) {
        notNullOf(id, "${entityName?uncap_first}Id");
        ${entityName} ${entityName?uncap_first} = new ${entityName}();
        ${entityName?uncap_first}.setId(id);
        ${entityName?uncap_first}.setDelFlag(BaseBean.DEL_FLAG_DELETE);
        return ${entityName?uncap_first}Dao.updateByPrimaryKeySelective(${entityName?uncap_first});
    }
<#-- Service delete with physical  -->
<#else>
    @Override
    public int del(Long id) {
        notNullOf(id, "${entityName?uncap_first}Id");
        return ${entityName?uncap_first}Dao.deleteByPrimaryKey(id);
    }
</#if>

}
