from django.urls import path
from .views import (
    LoginView, CadastroView, VincularGoogleView,
    ConverterView, PerfilView
)

urlpatterns = [
    path("auth/login", LoginView.as_view(), name="login"),
    path("auth/cadastro", CadastroView.as_view(), name="cadastrar-usuario"),
    path("auth/<uuid:user_id>/vincular-google", VincularGoogleView.as_view(), name="vincular-google"),

    path("api/v1/converter/", ConverterView.as_view(), name="converter-arquivo"),

    path('auth/<uuid:user_id>/perfil', PerfilView.as_view(), name='perfil-usuario'),

]
