from django.db import models


class Collection(models.Model):
    name = models.CharField(max_length=100)

    def __str__(self):
        return self.name


class InterestPoint(models.Model):
    name = models.CharField(max_length=100)
    scientific_name = models.CharField(max_length=100, null=True, blank=True)
    lat = models.DecimalField(max_digits=9, decimal_places=6)
    lng = models.DecimalField(max_digits=9, decimal_places=6)
    description = models.TextField(blank=True, max_length=500)
    collection = models.ForeignKey(Collection, on_delete=models.CASCADE, null=True, blank=True)

    def __str__(self):
        return self.name