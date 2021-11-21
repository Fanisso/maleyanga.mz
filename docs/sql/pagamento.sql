SELECT 
  cliente.id
FROM 
  public.cliente, 
  public.credito, 
  public.parcela, 
  public.pagamento
WHERE 
  cliente.id = credito.cliente_id AND 
  credito.id = pagamento.credito_id AND 
  pagamento.id = parcela.pagamento_id;
