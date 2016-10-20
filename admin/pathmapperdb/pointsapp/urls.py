from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
from pointsapp import views

urlpatterns = [
    url(r'^points/$', views.PointList.as_view()),
]

urlpatterns = format_suffix_patterns(urlpatterns)