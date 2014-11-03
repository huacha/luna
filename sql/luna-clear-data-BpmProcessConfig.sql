SET FOREIGN_KEY_CHECKS = 0;

delete from  BPM_CATEGORY             ;
delete from  BPM_CONF_BASE            ;
delete from  BPM_CONF_COUNTERSIGN     ;
delete from  BPM_CONF_FORM            ;
delete from  BPM_CONF_LISTENER        ;
delete from  BPM_CONF_NODE            ;
delete from  BPM_CONF_NOTICE          ;
delete from  BPM_CONF_OPERATION       ;
delete from  BPM_CONF_RULE            ;
delete from  BPM_CONF_USER            ;
delete from  BPM_DELEGATE_HISTORY     ;
delete from  BPM_DELEGATE_INFO        ;
delete from  BPM_PROCESS              ;
delete from  BPM_TASK_BUSSINESS       ;
delete from  BPM_TASK_CONF            ;
delete from  BPM_TASK_DEF             ;
delete from  BPM_TASK_DEF_NOTICE      ;
delete from  FORM_TEMPLATE            ;

commit;

SET FOREIGN_KEY_CHECKS = 1;