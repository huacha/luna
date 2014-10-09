<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="panel">

    <es:showMessage/>

    <div class="row-fluid tool ui-toolbar">
        <div class="span4">
            <div class="btn-group">
                <shiro:hasPermission name="bpm:process:delegate:create">
                <a class="btn btn-create">
                    <i class="icon-file-alt"></i>
                    新增
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bpm:process:delegate:update">
                <a id="update" class="btn btn-update">
                    <i class="icon-edit"></i>
                    修改
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bpm:process:delegate:delete">
                <a class="btn btn-delete">
                    <i class="icon-trash"></i>
                    删除
                </a>
                </shiro:hasPermission>
            </div>
        </div>
    </div>
    <%@include file="listTable.jsp"%>

</div>

<es:contentFooter/>
