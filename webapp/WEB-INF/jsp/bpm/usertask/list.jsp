<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="panel">

    <ul class="nav nav-tabs">
        <li ${param['taskstatus'] eq 'prepare' ? 'class="active"' : ''}>
            <a href="${ctx}/bpm/usertask?taskstatus=prepare">
                <i class="icon-table"></i>
                待办任务
            </a>
        </li>
        <li ${param['taskstatus'] eq 'preclaim' ? 'class="active"' : ''}>
            <a href="${ctx}/bpm/usertask?taskstatus=preclaim">
                <i class="icon-table"></i>
                待领任务
            </a>
        </li>
        <li ${param['taskstatus'] eq 'finished' ? 'class="active"' : ''}>
            <a href="${ctx}/bpm/usertask?taskstatus=finished">
                <i class="icon-table"></i>
                已完成任务
            </a>
        </li>
        <li ${param['taskstatus'] eq 'claimed' ? 'class="active"' : ''}>
            <a href="${ctx}/bpm/usertask?taskstatus=claimed">
                <i class="icon-table"></i>
                代理中的任务
            </a>
        </li>
    </ul>

    <es:showMessage/>

    <%@include file="listTable.jsp"%>

</div>

<es:contentFooter/>