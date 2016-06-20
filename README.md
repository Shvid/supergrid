# supergrid

### Simple key-value data access

* key is always String
* value is always ByteString

### Data affinity

* Cross-datacenter data distribution by SuperKey with near-cache (null - all DC)
* Partitioned data distribution by MajorKey inside DC (null - replicated distribution)
* Column-based data distribution by MinorKey inside Row (null - default column)

### Definitions

* Keyspace name is always lower case
* Cache name is always upper case

### Storage technologies

* SuperKey never stored, basically needs only for routing between DCs
* Client connects only to nearest DC
* Only not null MajorKey is using for hashing and partitioning
* Only hash of MajorKey stored in-memory, that reference to sorted_list[cacheId, offset, size, ttl] 
* Each cache has table in memory for MinorKeys [columnId <-> MinorKeys]

### Transactions

* No transactions support
* Each Row has version
* CAS (compare and set) method supported per Row
