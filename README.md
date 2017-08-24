# Purpose
A small utility to report disk utilization to Prometheus.

# What does it solve
Kubernetes prometheus monitoring won't monitor EBS volumes. Run this as a sidecar container to do just that.
```
 - name: ebs-metrics-sidecar
        image: gardnervickers/prometheus-ebs-metrics:latest
        imagePullPolicy: Always
        ports:
        - name: ebsmetrics
          containerPort: 4567
        args: ["-p", "4567", "-v", "datadir:/datadir"]
        volumeMounts:
        - name: datadir
          mountPath: /datadir
          readOnly: true
```
# How to run
As args to the container
- `-p`: Port to serve metrics on.
- `-v`: Volume name (as reported to Prometheus) and volume path, seperated by a colon `:`.
You can supply any number of `-v` args to monitor multiple volumes.
