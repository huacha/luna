<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form id="searchForm" class="form-inline search-form" data-change-search="true">


    <esform:label path="search.id_in">编号</esform:label>
    <esform:input path="search.id_in" cssClass="input-small" placeholder="多个使用空格分隔"/>
    &nbsp;&nbsp;

    <esform:label path="search.name_like">名称</esform:label>
    <esform:input path="search.name_like" cssClass="input-small" placeholder="模糊匹配"/>

    &nbsp;&nbsp;

    <esform:label path="search.show_eq">是否显示</esform:label>
    <esform:select path="search.show_eq" cssClass="input-small">
        <esform:option label="全部" value=""/>
        <esform:options items="${booleanList}" itemLabel="info"/>
    </esform:select>


    <button type="submit" class="btn ">查询</button>
    <a class="btn btn-link btn-clear-search">清空</a>
 
</form>