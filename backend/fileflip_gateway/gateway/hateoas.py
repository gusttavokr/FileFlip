def add_hateoas_links(data, request, resource_type, resource_id=None):
    if not isinstance(data, dict):
        return data

    base_url = f"{request.scheme}://{request.get_host()}"
    links = {}

    # self sempre é a URL da requisição atual
    links["self"] = {
        "href": request.build_absolute_uri(),
        "method": request.method,
    }

    # 1) RESPOSTA DE LOGIN:
    if resource_type == "login":
        user_id = data.get("userId") or data.get("id") or resource_id
        if user_id:
            links.pop("self", None)  

            links["perfil"] = {
                "href": f"{base_url}/gateway/auth/{user_id}/perfil",
                "method": "GET",
            }
            links["converter-arquivo"] = {
                "href": f"{base_url}/gateway/arquivo/converter",
                "method": "POST",
            }
            links["vincular-google"] = {
                "href": f"{base_url}/gateway/auth/{user_id}/vincular-google",
                "method": "POST",
            }

    # 2) RESPOSTA DE CADASTRO (usuario):
    elif resource_type == "usuario":
        user_id = data.get("id") or resource_id
        if user_id:
            links["login"] = {
                "href": f"{base_url}/gateway/auth/login",
                "method": "POST",
            }

    elif resource_type == "perfil":
        user_id = data.get("id") or resource_id
        if user_id:
            links["vincular-google"] = {
                "href": f"{base_url}/gateway/auth/{user_id}/vincular-google",
                "method": "POST",
            }
            links["converter-arquivo"] = {
                "href": f"{base_url}/gateway/converter",
                "method": "POST",
            }

    # 4) VINCULAR GOOGLE: mostrar perfil e converter
    elif resource_type == "vincular-google":
        user_id = resource_id or data.get("userId") or data.get("id")
        if user_id:
            links.pop("self", None)
            links["perfil"] = {
                "href": f"{base_url}/gateway/auth/{user_id}/perfil",
                "method": "GET",
            }
            links["converter-arquivo"] = {
                "href": f"{base_url}/gateway/converter",
                "method": "POST",
            }

    # 5) CONVERSAO: converter-novamente
    elif resource_type == "conversao":
        conversao_id = data.get("id") or resource_id

    data["_links"] = links
    return data


def add_collection_links(items, request, resource_type):
    if not isinstance(items, list):
        return items

    base_url = f"{request.scheme}://{request.get_host()}"

    result = {
        "items": items,
        "count": len(items),
        "_links": {
            "self": {
                "href": f"{base_url}{request.path}",
                "method": request.method,
            }
        },
    }

    return result
