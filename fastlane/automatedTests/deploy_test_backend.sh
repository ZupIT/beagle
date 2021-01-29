#!/bin/bash

# Send image to ECR
FULL_IMAGE=$(echo "$REGISTRY/$NAME:$VERSION" | tr [:upper:] [:lower:])
docker tag $NAME $FULL_IMAGE
docker push $FULL_IMAGE
docker logout $REGISTRY

# Deploy image in EKS
aws eks update-kubeconfig --name $CLUSTER
kubectl -n $NAMESPACE apply -f - << EOF
apiVersion: apps/v1
kind: Deployment
metadata:
  name: $NAME
  labels:
    app: $NAME
    version: $VERSION
spec:
  replicas: 1
  selector:
    matchLabels:
      app: $NAME
      version: $VERSION
  template:
    metadata:
      labels:
        app: $NAME
        version: $VERSION
    spec:
      containers:
        - name: $NAME
          image: $FULL_IMAGE
          livenessProbe:
            failureThreshold: 3
            httpGet:
              path: /actuator/health
              port: 8080
              scheme: HTTP
            initialDelaySeconds: 30
            periodSeconds: 20
            successThreshold: 1
            timeoutSeconds: 1
          readinessProbe:
            failureThreshold: 3
            httpGet:
              path: /actuator/health
              port: 8080
              scheme: HTTP
            initialDelaySeconds: 30
            periodSeconds: 20
            successThreshold: 1
            timeoutSeconds: 1
          imagePullPolicy: Always

---

apiVersion: v1
kind: Service
metadata:
  labels:
    app: $NAME
    service: $NAME
  name: $NAME
spec:
  ports:
    - name: http
      port: 8080
      targetPort: 8080
  selector:
    app: $NAME
EOF
