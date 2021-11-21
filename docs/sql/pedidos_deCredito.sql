SELECT 
  cliente.email, 
  cliente.nome, 
  cliente.telefone, 
  cliente.residencia, 
  pedido_de_credito.creditado, 
  pedido_de_credito.data_de_aprovacao, 
  pedido_de_credito.descricao_da_penhora, 
  pedido_de_credito.estado, 
  pedido_de_credito.motivo, 
  pedido_de_credito.valor_de_credito, 
  pedido_de_credito.valor_da_penhora
FROM 
  public.pedido_de_credito, 
  public.cliente
WHERE 
  cliente.id = pedido_de_credito.cliente_id;
