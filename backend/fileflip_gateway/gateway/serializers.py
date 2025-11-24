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
