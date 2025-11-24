from zeep import Client

class SoapClient:
    def __init__(self):
        self.client = Client("http://localhost:8083/ws?wsdl")
