import global from "../../common/global_variable";

export default [

    {
        name: '${entityName?uncap_first}List',
        method: '${entityName?uncap_first}List',
        path: '/${entityName?lower_case}/list',
        type: 'post',
        sys: global.${moduleName?lower_case}
    },
    {
        name: 'save${entityName?cap_first}',
        method: 'save${entityName?cap_first}',
        path: '/${entityName?lower_case}/save',
        type: 'json',
        sys: global.${moduleName?lower_case}
    },
    {
        name: '${entityName?uncap_first}Detail',
        method: '${entityName?uncap_first}Detail',
        path: '/${entityName?lower_case}/detail',
        type: 'post',
        sys: global.${moduleName?lower_case}
    },
    {
        name: 'del${entityName?cap_first}',
        method: 'del${entityName?cap_first}',
        path: '/${entityName?lower_case}/del',
        type: 'post',
        sys: global.${moduleName?lower_case}
    },



]


