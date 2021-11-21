SELECT 
  pagamento.id
FROM 
  public.pagamento, 
  public.parcela, 
  public.remissao
WHERE 
  pagamento.id = parcela.pagamento_id AND
  pagamento.id = remissao.pagamento_id AND
  pagamento.id = ;
