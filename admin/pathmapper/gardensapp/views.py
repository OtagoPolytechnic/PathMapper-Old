from django.shortcuts import render

from django.core import serializers
from django.http import HttpResponse
from gardensapp.models import Plant
from gardensapp.models import Collection
from gardensapp.models import Track
from gardensapp.models import PlantCollection
from gardensapp.models import UpdateManager

def plant_json(request):
    item = Plant.objects.all()
    data = serializers.serialize("json", item)
    return HttpResponse(data, content_type='application/json')

def collection_json(request):
    item = Collection.objects.all()
    data = serializers.serialize("json", item)
    return HttpResponse(data, content_type='application/json')

def track_json(request):
    item = Track.objects.all()
    data = serializers.serialize("json", item)
    return HttpResponse(data, content_type='application/json')

def updateManager_json(request):
    item = UpdateManager.objects.all()
    data = serializers.serialize("json", item)
    return HttpResponse(data, content_type='application/json')

def plantCollection_json(request):
    item = PlantCollection.objects.all()
    data = serializers.serialize("json", item)
    return HttpResponse(data, content_type='application/json')