from django.conf.urls import url
from django.contrib import admin
from gardensapp import views

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^jsonplant/', views.plant_json),
    url(r'^jsoncollection/', views.collection_json),
    url(r'^jsonplantcollection/', views.plantCollection_json),
    url(r'^jsontrack/', views.track_json),
    url(r'^jsonupdate/', views.updateManager_json),
]