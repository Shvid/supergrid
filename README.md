# supergrid

### Phase

Current phase of the project: idea, far away from PoC

### Simple key-value data access

* key is always String
* value is always ByteString

### Data affinity

* Cross-datacenter data distribution by SuperKey with near-cache (null - all DC)
* Partitioned data distribution by MajorKey (null - replicated distribution)
* Tier-based data distribution by MinorKey (null - Tier0, not null - Tier1)

### Tiers

* SuperKey - always in memory and server configurations as it is
* MajorKey - always in memory as a hash
* Tier0 - in memory both key and value
* Tier1 - on disk both key and value

### Definitions

* Keyspace name is always lower case
* Cache name is always upper case
* TTL per major key
* EntityId per minor key

### Storage technologies

* SuperKey needs for routing between DCs
* Client connects to nearest DC
* MajorKey is using for hashing and partitioning
* MajorKey stored in-memory, that reference to sorted_list[cacheId, offset, size, ttl, version] 

### Transactions

* No transactions
* Each majorKey entry has a version
* CAS (compare and set) method supported per MajorKey access
