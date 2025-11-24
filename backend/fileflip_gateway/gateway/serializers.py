from rest_framework import serializers

class GenericBodySerializer(serializers.Serializer):
    # Aceita qualquer corpo (igual Map<String, Object> do Java)
    def to_internal_value(self, data):
        return data
