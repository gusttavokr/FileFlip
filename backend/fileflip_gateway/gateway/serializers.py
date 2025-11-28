from rest_framework import serializers

class LoginRequestSerializer(serializers.Serializer):
    email = serializers.EmailField()
    password = serializers.CharField()

class LoginResponseSerializer(serializers.Serializer):
    token = serializers.CharField()
    email = serializers.EmailField()
    username = serializers.CharField()

class UsuarioRequestSerializer(serializers.Serializer):
    username = serializers.CharField(min_length=3, max_length=100)
    email = serializers.EmailField()
    password = serializers.CharField(min_length=6)
    confirmPassword = serializers.CharField(min_length=6)

class UsuarioResponseSerializer(serializers.Serializer):
    id = serializers.UUIDField()
    username = serializers.CharField()
    email = serializers.EmailField()
    googleVinculado = serializers.BooleanField()
    googleName = serializers.CharField(required=False, allow_null=True)
    googlePictureUrl = serializers.CharField(required=False, allow_null=True)

class VincularGoogleRequestSerializer(serializers.Serializer):
    googleAccessToken = serializers.CharField()
    googleRefreshToken = serializers.CharField()
    googleId = serializers.CharField()
    googleName = serializers.CharField()
    googlePictureUrl = serializers.CharField()

class VincularGoogleResponseSerializer(serializers.Serializer):
    googleId = serializers.CharField()
    googleName = serializers.CharField()
    googlePictureUrl = serializers.CharField()
    googleVinculado = serializers.BooleanField()


ARQUIVO_TIPOS = [
    "PDF",
    "TXT",
    "DOCX",
    "DOC",
    "JPG",
    "JPEG",
    "PNG",
    "SVG",
    "WEBP",
    "MP4",
    "MP3",
    "WAV",
    "M4A",
    "ZIP",
    "RAR",
]

class ConversaoRequestSerializer(serializers.Serializer):
    novoTipo = serializers.ChoiceField(
        choices=ARQUIVO_TIPOS,
        help_text="Tipo do novo arquivo (mesmos valores do enum ArquivoType do backend)",
    )
    arquivo = serializers.FileField(
        required=True,
        help_text="Dados do arquivo original",
    )


class ConversaoResponseSerializer(serializers.Serializer):
    mensagem = serializers.CharField(
        help_text="Mensagem retornada pelo serviço de conversão"
    )
    urlDownload = serializers.CharField(
        help_text="URL para download do arquivo convertido"
    )


class ArquivoResponseSerializer(serializers.Serializer):
    arquivo_id = serializers.UUIDField()
    name = serializers.CharField()
    tamanhoArquivo = serializers.IntegerField()
    possuiFoto = serializers.BooleanField()
    usuario_id = serializers.UUIDField()
    url_download = serializers.CharField()

class PerfilcomArquivosSerializer(serializers.Serializer):
    usuario = UsuarioResponseSerializer()
    arquivos = ArquivoResponseSerializer(many=True)