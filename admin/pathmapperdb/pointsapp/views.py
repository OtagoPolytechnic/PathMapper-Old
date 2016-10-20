from django.shortcuts import render
from pointsapp.models import InterestPoint, Collection
from pointsapp.serializers import PointSerializer, CollectionSerializer
from django.http import Http404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status

class PointList(APIView):
    def get(self, request, format=None):
        points = InterestPoint.objects.all()
        collections = Collection.objects.all()

        pointSerializer = PointSerializer(points, many=True)
        collectionSerializer = CollectionSerializer(collections, many=True)

        return Response({
            'InterestPoints': pointSerializer.data,
            'Collections': collectionSerializer.data,
        })
