<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form id="searchForm" class="form-inline search-form" data-change-search="true">

    <esform:label path="search.id_in">编号</esform:label>
    <esform:input path="search.id_in" cssClass="input-small" placeholder="多个使用空格分隔"/>
    &nbsp;
    <esform:label path="search.processDefinitionKey_like">流程键值</esform:label>
    <esform:input path="search.processDefinitionKey_like" cssClass="input-middle" placeholder="模糊查询"/>
    &nbsp;&nbsp;
    <input type="submit" class="btn " value="查询"/>
    <a class="btn btn-link btn-clear-search">清空</a>
</form>