SELECT 
  utilizador.username, 
  action_logging_event.action_name, 
  action_logging_event.controller_name, 
  action_logging_event.custom_action_name, 
  action_logging_event.custom_log, 
  action_logging_event.date, 
  action_logging_event.end_time, 
  action_logging_event.params, 
  action_logging_event.remote_host, 
  action_logging_event.start_time, 
  action_logging_event.total_time, 
  action_logging_event.status
FROM 
  public.action_logging_event, 
  public.utilizador
WHERE 
  action_logging_event.user_id = utilizador.id;
