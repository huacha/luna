<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="panel">


    <es:showMessage/>

    <div class="row-fluid tool ui-toolbar">
        <div class="span4">
            <div class="btn-group">
                <a class="btn btn-create">
                    <i class="icon-file-alt"></i>
                    新增
                </a>
            </div>
        </div>
        <div class="span8">
            
        </div>
    </div>
    <%@include file="listTable.jsp"%>

</div>

<es:contentFooter/>