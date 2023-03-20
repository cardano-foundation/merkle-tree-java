use aiken/builtin
use aiken/bytearray
use aiken/hash.{Hash, Sha2_256, sha2_256}
use aiken/list

pub type HashedList<a> {
  inner: List<Hash<Sha2_256, a>>,
  hash: Hash<Sha2_256, a>,
}

fn combine_hash(
  left: Hash<Sha2_256, a>,
  right: Hash<Sha2_256, a>,
) -> Hash<Sha2_256, a> {
  sha2_256(bytearray.concat(left, right))
}

pub fn from(
  items: List<a>,
  hash_fn: fn(a) -> Hash<Sha2_256, a>,
) -> HashedList<a> {
  let hashed_items =
    items
      |> list.map(fn(item) { hash_fn(item) })

  let zero =
    sha2_256(#"")

  let reducer =
    fn(a, b) { combine_hash(a, b) }

  let hash =
    list.foldr(hashed_items, reducer, zero)

  HashedList { inner: hashed_items, hash }
}

test from_1() {
  let items =
    []

  let hashed_list =
    from(items, create_string_item_serialise_fn())

  hashed_list.hash == #"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
}

test from_2() {
  let dog =
    @"dog"
  let items =
    [dog]

  let hashed_list =
    from(items, create_string_item_serialise_fn())

  hashed_list.hash == #"c333744e9953ea4990e00a00806a596f6b1a0fc39a8fdf2e9e9bfa622fb58099"
}

test from_3() {
  let dog =
    @"dog"
  let cat =
    @"cat"
  let items =
    [dog, cat]

  let hashed_list =
    from(items, create_string_item_serialise_fn())

  hashed_list.hash == #"9e0fa7d9199bb3f606f53c2ccb548fc1e0181eccb913eeb645eaee7c1b00c0be"
}

test fold() {
  let chars =
    [@"a", @"b", @"c"]

  let zero =
    @"0"

  let reducer =
    fn(a, b) { builtin.append_string(a, b) }

  @"0abc" == list.foldr(chars, reducer, zero)
}

fn create_string_item_serialise_fn() -> fn(String) -> ByteArray {
  fn(x: String) { sha2_256(bytearray.from_string(x)) }
}
