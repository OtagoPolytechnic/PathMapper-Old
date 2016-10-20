from rest_framework import serializers
from pointsapp.models import InterestPoint, Collection


class PointSerializer(serializers.ModelSerializer):
    class Meta:
        model = InterestPoint
        fields = ('id', 'name', 'scientific_name', 'lat', 'lng', 'description', 'collection')

class CollectionSerializer(serializers.ModelSerializer):
    class Meta:
        model = Collection
        fields = ('id', 'name')
