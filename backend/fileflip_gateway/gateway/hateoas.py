"""
Utilitário para adicionar links HATEOAS nas respostas da API Gateway
"""
from django.urls import reverse


def add_hateoas_links(data, request, resource_type, resource_id=None):
    if not isinstance(data, dict):
        return data

    base_url = f"{request.scheme}://{request.get_host()}"
    links = {}

    # link self genérico
    links["self"] = {
        "href": request.build_absolute_uri(),  # URL da requisição atual
        "method": request.method,
    }

    if resource_type == "login":
        user_id = data.get("userId") or data.get("id")
        if user_id:
            links["vincular-google"] = {
                "href": f"{base_url}/gateway/auth/usuarios/{user_id}/vincular-google",
                "method": "POST",
            }
            links["converter-arquivo"] = {
                "href": f"{base_url}/gateway/arquivo/converter",
                "method": "POST",
            }

    # mantém sua lógica de usuario / vincular-google / conversao / historico aqui...

    data["_links"] = links
    return data



def add_collection_links(items, request, resource_type):
    """
    Adiciona links HATEOAS para uma coleção de recursos
    
    Args:
        items: Lista de recursos
        request: HttpRequest object
        resource_type: Tipo do recurso
    
    Returns:
        Dict com items + _links
    """
    if not isinstance(items, list):
        return items
    
    base_url = f"{request.scheme}://{request.get_host()}"
    
    result = {
        'items': items,
        'count': len(items),
        '_links': {
            'self': {
                'href': f"{base_url}{request.path}",
                'method': request.method
            }
        }
    }
    
    # Adiciona links específicos para cada tipo de coleção
    if resource_type == 'historico':
        result['_links']['converter'] = {
            'href': f"{base_url}/gateway/arquivo/converter",
            'method': 'POST'
        }
    
    return result
