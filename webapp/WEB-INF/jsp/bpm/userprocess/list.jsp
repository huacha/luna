<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="panel">

    <ul class="nav nav-tabs">
        <li ${param['processstatus'] eq 'involve' ? 'class="active"' : ''}>
            <a href="${ctx}/bpm/userprocess?processstatus=involve">
                <i class="icon-table"></i>
                参与的流程
            </a>
        </li>
        <li ${param['processstatus'] eq 'unfinished' ? 'class="active"' : ''}>
            <a href="${ctx}/bpm/userprocess?processstatus=unfinished">
                <i class="icon-table"></i>
                未结流程
            </a>
        </li>
        <li ${param['processstatus'] eq 'finished' ? 'class="active"' : ''}>
            <a href="${ctx}/bpm/userprocess?processstatus=finished">
                <i class="icon-table"></i>
                已结流程
            </a>
        </li>
    </ul>

    <es:showMessage/>

    <%@include file="listTable.jsp"%>

</div>

<es:contentFooter/>