import httpx

class ClientsConfig:

    def __init__(self):
        self.arquivo_client = httpx.Client(base_url="http://localhost:8082")
        self.auth_client = httpx.Client(base_url="http://localhost:8081")
