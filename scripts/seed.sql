--
-- PostgreSQL database dump
--

-- Dumped from database version 13.11
-- Dumped by pg_dump version 15.3

-- Started on 2024-03-16 17:11:51 IST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: madhusudhanan
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO madhusudhanan;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 200 (class 1259 OID 208840)
-- Name: cart_items; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.cart_items (
    id bigint NOT NULL,
    cart_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity integer DEFAULT 1 NOT NULL,
    status text NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.cart_items OWNER TO madhusudhanan;

--
-- TOC entry 201 (class 1259 OID 208849)
-- Name: cart_items_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.cart_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.cart_items_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3435 (class 0 OID 0)
-- Dependencies: 201
-- Name: cart_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.cart_items_id_seq OWNED BY public.cart_items.id;


--
-- TOC entry 202 (class 1259 OID 208851)
-- Name: carts; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.carts (
    id bigint NOT NULL,
    user_id text NOT NULL,
    status text NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.carts OWNER TO madhusudhanan;

--
-- TOC entry 203 (class 1259 OID 208859)
-- Name: carts_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.carts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.carts_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 203
-- Name: carts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.carts_id_seq OWNED BY public.carts.id;


--
-- TOC entry 204 (class 1259 OID 208861)
-- Name: categories; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.categories (
    id bigint NOT NULL,
    name text NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.categories OWNER TO madhusudhanan;

--
-- TOC entry 205 (class 1259 OID 208869)
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.categories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.categories_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 205
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.categories_id_seq OWNED BY public.categories.id;


--
-- TOC entry 206 (class 1259 OID 208871)
-- Name: order_items; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.order_items (
    id bigint NOT NULL,
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity integer NOT NULL,
    unit_price double precision DEFAULT 0.00,
    net_amount double precision DEFAULT 0.00,
    tax_amount double precision DEFAULT 0.00,
    gross_amount double precision DEFAULT 0.00,
    tax_percentage numeric(2,2) DEFAULT 0.00,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.order_items OWNER TO madhusudhanan;

--
-- TOC entry 207 (class 1259 OID 208881)
-- Name: order_items_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.order_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.order_items_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 207
-- Name: order_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.order_items_id_seq OWNED BY public.order_items.id;


--
-- TOC entry 208 (class 1259 OID 208883)
-- Name: orders; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.orders (
    id bigint NOT NULL,
    user_id text NOT NULL,
    status text NOT NULL,
    net_total double precision DEFAULT 0.00,
    gross_total double precision DEFAULT 0.00,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.orders OWNER TO madhusudhanan;

--
-- TOC entry 209 (class 1259 OID 208893)
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.orders_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 209
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- TOC entry 210 (class 1259 OID 208895)
-- Name: product_attributes; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.product_attributes (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    attribute_name text,
    attribute_value text,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.product_attributes OWNER TO madhusudhanan;

--
-- TOC entry 211 (class 1259 OID 208903)
-- Name: product_attributes_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.product_attributes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.product_attributes_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 211
-- Name: product_attributes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.product_attributes_id_seq OWNED BY public.product_attributes.id;


--
-- TOC entry 212 (class 1259 OID 208905)
-- Name: products; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.products (
    id bigint NOT NULL,
    category_id bigint NOT NULL,
    name text NOT NULL,
    description text,
    is_available boolean NOT NULL,
    sku_code text NOT NULL,
    stock_quantity integer DEFAULT 0 NOT NULL,
    unit_price double precision DEFAULT 0.00 NOT NULL,
    gross_amount double precision DEFAULT 0.00 NOT NULL,
    tax_percentage double precision DEFAULT 0.00 NOT NULL,
    tax_amount double precision DEFAULT 0.00 NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.products OWNER TO madhusudhanan;

--
-- TOC entry 213 (class 1259 OID 208918)
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.products_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 213
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- TOC entry 214 (class 1259 OID 208920)
-- Name: schema_version; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.schema_version (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.schema_version OWNER TO madhusudhanan;

--
-- TOC entry 217 (class 1259 OID 208937)
-- Name: user_addresses; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.user_addresses (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    address_1 text,
    address_2 text,
    city text,
    state text,
    zip_code text,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_default boolean NOT NULL
);


ALTER TABLE public.user_addresses OWNER TO madhusudhanan;

--
-- TOC entry 215 (class 1259 OID 208927)
-- Name: user_pool; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.user_pool (
    id bigint NOT NULL,
    cognito_sub text NOT NULL,
    email text NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.user_pool OWNER TO madhusudhanan;

--
-- TOC entry 216 (class 1259 OID 208935)
-- Name: user_pool_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.user_pool_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_pool_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3442 (class 0 OID 0)
-- Dependencies: 216
-- Name: user_pool_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.user_pool_id_seq OWNED BY public.user_pool.id;


--
-- TOC entry 218 (class 1259 OID 208945)
-- Name: user_profile_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.user_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_profile_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3443 (class 0 OID 0)
-- Dependencies: 218
-- Name: user_profile_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.user_profile_id_seq OWNED BY public.user_addresses.id;


--
-- TOC entry 3191 (class 2604 OID 208947)
-- Name: cart_items id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.cart_items ALTER COLUMN id SET DEFAULT nextval('public.cart_items_id_seq'::regclass);


--
-- TOC entry 3195 (class 2604 OID 208948)
-- Name: carts id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.carts ALTER COLUMN id SET DEFAULT nextval('public.carts_id_seq'::regclass);


--
-- TOC entry 3198 (class 2604 OID 208949)
-- Name: categories id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.categories ALTER COLUMN id SET DEFAULT nextval('public.categories_id_seq'::regclass);


--
-- TOC entry 3201 (class 2604 OID 208950)
-- Name: order_items id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.order_items ALTER COLUMN id SET DEFAULT nextval('public.order_items_id_seq'::regclass);


--
-- TOC entry 3209 (class 2604 OID 208951)
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- TOC entry 3214 (class 2604 OID 208952)
-- Name: product_attributes id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.product_attributes ALTER COLUMN id SET DEFAULT nextval('public.product_attributes_id_seq'::regclass);


--
-- TOC entry 3217 (class 2604 OID 208953)
-- Name: products id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- TOC entry 3229 (class 2604 OID 208955)
-- Name: user_addresses id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_addresses ALTER COLUMN id SET DEFAULT nextval('public.user_profile_id_seq'::regclass);


--
-- TOC entry 3226 (class 2604 OID 208954)
-- Name: user_pool id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_pool ALTER COLUMN id SET DEFAULT nextval('public.user_pool_id_seq'::regclass);


--
-- TOC entry 3410 (class 0 OID 208840)
-- Dependencies: 200
-- Data for Name: cart_items; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.cart_items (id, cart_id, product_id, quantity, status, created_at, updated_at) FROM stdin;
\.


--
-- TOC entry 3412 (class 0 OID 208851)
-- Dependencies: 202
-- Data for Name: carts; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.carts (id, user_id, status, created_at, updated_at) FROM stdin;
\.


--
-- TOC entry 3414 (class 0 OID 208861)
-- Dependencies: 204
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.categories (id, name, created_at, updated_at) FROM stdin;
1	Electronics	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
2	Apparel	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
3	Home & Garden	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
4	Health & Beauty	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
5	Toys & Games	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
6	Books & Media	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
7	Sports & Outdoors	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
8	Automotive	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
9	Office Supplies	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
10	Pet Supplies	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
11	Jewelry & Watches	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
12	Baby & Kids	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
13	Healthcare & Medical	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
14	Crafts & Hobbies	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
15	Travel & Luggage	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
16	Electrical & Lighting	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
17	Industrial & Scientific	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
18	Gifts & Specialty	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
\.


--
-- TOC entry 3416 (class 0 OID 208871)
-- Dependencies: 206
-- Data for Name: order_items; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.order_items (id, order_id, product_id, quantity, unit_price, net_amount, tax_amount, gross_amount, tax_percentage, created_at, updated_at) FROM stdin;
\.


--
-- TOC entry 3418 (class 0 OID 208883)
-- Dependencies: 208
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.orders (id, user_id, status, net_total, gross_total, created_at, updated_at) FROM stdin;
\.


--
-- TOC entry 3420 (class 0 OID 208895)
-- Dependencies: 210
-- Data for Name: product_attributes; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.product_attributes (id, product_id, attribute_name, attribute_value, created_at, updated_at) FROM stdin;
1	1	version	15	2024-03-16 15:55:17.883148+05:30	2024-03-16 15:55:17.883186+05:30
2	1	color	blue	2024-03-16 15:55:17.887228+05:30	2024-03-16 15:55:17.88726+05:30
3	1	size	128 GB	2024-03-16 15:55:17.888381+05:30	2024-03-16 15:55:17.888408+05:30
4	2	version	15	2024-03-16 15:56:12.745319+05:30	2024-03-16 15:56:12.745344+05:30
5	2	color	lilac	2024-03-16 15:56:12.746316+05:30	2024-03-16 15:56:12.746338+05:30
6	2	size	128 GB	2024-03-16 15:56:12.747751+05:30	2024-03-16 15:56:12.747774+05:30
7	3	version	15	2024-03-16 15:56:33.439169+05:30	2024-03-16 15:56:33.439191+05:30
8	3	color	lilac	2024-03-16 15:56:33.440152+05:30	2024-03-16 15:56:33.440174+05:30
9	3	size	256 GB	2024-03-16 15:56:33.441032+05:30	2024-03-16 15:56:33.44105+05:30
10	4	version	15	2024-03-16 15:56:39.380541+05:30	2024-03-16 15:56:39.38057+05:30
11	4	color	red	2024-03-16 15:56:39.381408+05:30	2024-03-16 15:56:39.381436+05:30
12	4	size	256 GB	2024-03-16 15:56:39.382341+05:30	2024-03-16 15:56:39.382364+05:30
13	5	Net Quantity	50 ml	2024-03-16 15:58:28.230313+05:30	2024-03-16 15:58:28.230344+05:30
14	5	Country of Origin	India	2024-03-16 15:58:28.231429+05:30	2024-03-16 15:58:28.231457+05:30
15	5	Item form	Lotion	2024-03-16 15:58:28.232649+05:30	2024-03-16 15:58:28.232674+05:30
16	5	Active Ingredients	Vitamin E, Panthenol (Vitamin B5), Niacinamide (Vitamin B3)	2024-03-16 15:58:28.233633+05:30	2024-03-16 15:58:28.233655+05:30
17	6	Net Quantity	250 ml	2024-03-16 15:59:39.045763+05:30	2024-03-16 15:59:39.04579+05:30
18	6	Country of Origin	India	2024-03-16 15:59:39.046881+05:30	2024-03-16 15:59:39.04691+05:30
19	6	Item form	Lotion	2024-03-16 15:59:39.048151+05:30	2024-03-16 15:59:39.048194+05:30
20	6	Active Ingredients	Vitamin E, Panthenol (Vitamin B5), Niacinamide (Vitamin B3)	2024-03-16 15:59:39.049315+05:30	2024-03-16 15:59:39.049355+05:30
21	7	Net Quantity	200 ml	2024-03-16 15:59:53.35719+05:30	2024-03-16 15:59:53.357215+05:30
22	7	Country of Origin	India	2024-03-16 15:59:53.357943+05:30	2024-03-16 15:59:53.35797+05:30
23	7	Item form	Lotion	2024-03-16 15:59:53.358656+05:30	2024-03-16 15:59:53.358679+05:30
24	7	Active Ingredients	Vitamin E, Panthenol (Vitamin B5), Niacinamide (Vitamin B3)	2024-03-16 15:59:53.3594+05:30	2024-03-16 15:59:53.359418+05:30
25	8	Net Quantity	1	2024-03-16 16:49:09.644125+05:30	2024-03-16 16:49:09.644158+05:30
26	8	Country of Origin	India	2024-03-16 16:49:09.645733+05:30	2024-03-16 16:49:09.645758+05:30
27	8	Material	Cotton	2024-03-16 16:49:09.646684+05:30	2024-03-16 16:49:09.646706+05:30
28	8	Size	S	2024-03-16 16:49:09.647562+05:30	2024-03-16 16:49:09.647585+05:30
29	8	Color	Blue	2024-03-16 16:49:09.648617+05:30	2024-03-16 16:49:09.648637+05:30
30	8	Pattern	Checked	2024-03-16 16:49:09.649836+05:30	2024-03-16 16:49:09.649856+05:30
31	10	Net Quantity	1	2024-03-16 16:49:49.565933+05:30	2024-03-16 16:49:49.565981+05:30
32	10	Country of Origin	India	2024-03-16 16:49:49.570765+05:30	2024-03-16 16:49:49.570804+05:30
33	10	Material	Cotton	2024-03-16 16:49:49.571792+05:30	2024-03-16 16:49:49.571813+05:30
34	10	Size	M	2024-03-16 16:49:49.572687+05:30	2024-03-16 16:49:49.572706+05:30
35	10	Color	Blue	2024-03-16 16:49:49.573783+05:30	2024-03-16 16:49:49.573808+05:30
36	10	Pattern	Checked	2024-03-16 16:49:49.574994+05:30	2024-03-16 16:49:49.575031+05:30
37	11	Net Quantity	1	2024-03-16 16:50:07.025496+05:30	2024-03-16 16:50:07.025521+05:30
38	11	Country of Origin	India	2024-03-16 16:50:07.026636+05:30	2024-03-16 16:50:07.026663+05:30
39	11	Material	Cotton	2024-03-16 16:50:07.027651+05:30	2024-03-16 16:50:07.027672+05:30
40	11	Size	M	2024-03-16 16:50:07.028605+05:30	2024-03-16 16:50:07.028626+05:30
41	11	Color	Blue	2024-03-16 16:50:07.029474+05:30	2024-03-16 16:50:07.029494+05:30
42	11	Pattern	Checked	2024-03-16 16:50:07.030376+05:30	2024-03-16 16:50:07.030396+05:30
\.


--
-- TOC entry 3422 (class 0 OID 208905)
-- Dependencies: 212
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.products (id, category_id, name, description, is_available, sku_code, stock_quantity, unit_price, gross_amount, tax_percentage, tax_amount, created_at, updated_at) FROM stdin;
1	1	iPhone 15 128 GB - Blue	\N	t	iPho/15/bl/128 	100	87000.98	97441.1	18	10440.12	2024-03-16 15:55:17.861711+05:30	2024-03-16 15:55:17.861772+05:30
2	1	iPhone 15 128 GB - Lilac	\N	t	iPho/15/lil/128 	100	87000.98	97441.1	18	10440.12	2024-03-16 15:56:12.743765+05:30	2024-03-16 15:56:12.743797+05:30
3	1	iPhone 15 256 GB - Lilac	\N	t	iPho/15/lil/256 	100	87000.98	97441.1	18	10440.12	2024-03-16 15:56:33.437581+05:30	2024-03-16 15:56:33.437601+05:30
4	1	iPhone 15 256 GB - Red	\N	t	iPho/15/r/256 	100	87000.98	97441.1	18	10440.12	2024-03-16 15:56:39.379169+05:30	2024-03-16 15:56:39.379197+05:30
5	4	Moisturiser 50 ml	\N	t	Mois/50 /Ind/Loti	100	200.45	224.5	12	24.05	2024-03-16 15:58:28.228736+05:30	2024-03-16 15:58:28.228764+05:30
6	4	Moisturiser 250 ml	\N	t	Mois/250 /Ind/Loti	100	400.45	448.5	12	48.05	2024-03-16 15:59:39.044125+05:30	2024-03-16 15:59:39.044151+05:30
7	4	Moisturiser 200 ml	\N	t	Mois/200 /Ind/Loti	100	350.45	392.5	12	42.05	2024-03-16 15:59:53.356178+05:30	2024-03-16 15:59:53.356197+05:30
8	2	Shirt Checked - S	\N	t	Shir/1/Ind/Cott	100	750.45	840.5	5	90.05	2024-03-16 16:49:09.630772+05:30	2024-03-16 16:49:09.630842+05:30
10	2	Checked - M	\N	t	Chec/1/Ind/Cott	100	750.45	840.5	5	90.05	2024-03-16 16:49:49.564123+05:30	2024-03-16 16:49:49.564156+05:30
11	2	Blue Checked - L	\N	t	Blue/1/Ind/Cott	100	750.45	840.5	5	90.05	2024-03-16 16:50:07.024056+05:30	2024-03-16 16:50:07.024084+05:30
\.


--
-- TOC entry 3424 (class 0 OID 208920)
-- Dependencies: 214
-- Data for Name: schema_version; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	create tables	SQL	V1__create_tables.sql	-712934250	madhusudhanan	2024-03-16 15:54:56.931695	121	t
2	2	create order shopping cart tables	SQL	V2__create_order_shopping_cart_tables.sql	349886666	madhusudhanan	2024-03-16 15:54:57.077329	82	t
\.


--
-- TOC entry 3427 (class 0 OID 208937)
-- Dependencies: 217
-- Data for Name: user_addresses; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.user_addresses (id, user_id, address_1, address_2, city, state, zip_code, created_at, updated_at, is_default) FROM stdin;
\.


--
-- TOC entry 3425 (class 0 OID 208927)
-- Dependencies: 215
-- Data for Name: user_pool; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.user_pool (id, cognito_sub, email, first_name, last_name, created_at, updated_at) FROM stdin;
\.


--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 201
-- Name: cart_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.cart_items_id_seq', 1, false);


--
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 203
-- Name: carts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.carts_id_seq', 1, false);


--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 205
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.categories_id_seq', 18, true);


--
-- TOC entry 3447 (class 0 OID 0)
-- Dependencies: 207
-- Name: order_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.order_items_id_seq', 1, false);


--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 209
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.orders_id_seq', 1, false);


--
-- TOC entry 3449 (class 0 OID 0)
-- Dependencies: 211
-- Name: product_attributes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.product_attributes_id_seq', 42, true);


--
-- TOC entry 3450 (class 0 OID 0)
-- Dependencies: 213
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.products_id_seq', 11, true);


--
-- TOC entry 3451 (class 0 OID 0)
-- Dependencies: 216
-- Name: user_pool_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.user_pool_id_seq', 1, false);


--
-- TOC entry 3452 (class 0 OID 0)
-- Dependencies: 218
-- Name: user_profile_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.user_profile_id_seq', 1, false);


--
-- TOC entry 3233 (class 2606 OID 208957)
-- Name: cart_items cart_items_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_pkey PRIMARY KEY (id);


--
-- TOC entry 3235 (class 2606 OID 208959)
-- Name: cart_items cart_product_unique; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_product_unique UNIQUE (cart_id, product_id);


--
-- TOC entry 3238 (class 2606 OID 208961)
-- Name: carts carts_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT carts_pkey PRIMARY KEY (id);


--
-- TOC entry 3243 (class 2606 OID 208963)
-- Name: categories categories_name_key; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_name_key UNIQUE (name);


--
-- TOC entry 3245 (class 2606 OID 208965)
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- TOC entry 3247 (class 2606 OID 208967)
-- Name: order_items order_items_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_pkey PRIMARY KEY (id);


--
-- TOC entry 3249 (class 2606 OID 208969)
-- Name: order_items order_items_unique; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_unique UNIQUE (order_id, product_id);


--
-- TOC entry 3252 (class 2606 OID 208971)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 3255 (class 2606 OID 208973)
-- Name: product_attributes product_attributes_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.product_attributes
    ADD CONSTRAINT product_attributes_pkey PRIMARY KEY (id);


--
-- TOC entry 3258 (class 2606 OID 208975)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 3260 (class 2606 OID 208977)
-- Name: products products_sku_code_key; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_sku_code_key UNIQUE (sku_code);


--
-- TOC entry 3262 (class 2606 OID 208979)
-- Name: schema_version schema_version_pk; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.schema_version
    ADD CONSTRAINT schema_version_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 3241 (class 2606 OID 208981)
-- Name: carts user_cart_unique; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT user_cart_unique UNIQUE (user_id);


--
-- TOC entry 3265 (class 2606 OID 208983)
-- Name: user_pool user_pool_cognito_sub_key; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_pool
    ADD CONSTRAINT user_pool_cognito_sub_key UNIQUE (cognito_sub);


--
-- TOC entry 3267 (class 2606 OID 208985)
-- Name: user_pool user_pool_email_key; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_pool
    ADD CONSTRAINT user_pool_email_key UNIQUE (email);


--
-- TOC entry 3269 (class 2606 OID 208987)
-- Name: user_pool user_pool_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_pool
    ADD CONSTRAINT user_pool_pkey PRIMARY KEY (id);


--
-- TOC entry 3272 (class 2606 OID 208989)
-- Name: user_addresses user_profile_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_addresses
    ADD CONSTRAINT user_profile_pkey PRIMARY KEY (id);


--
-- TOC entry 3236 (class 1259 OID 208990)
-- Name: idx_cart_items_cart_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_cart_items_cart_id ON public.cart_items USING btree (cart_id);


--
-- TOC entry 3239 (class 1259 OID 208991)
-- Name: idx_carts_user_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_carts_user_id ON public.carts USING btree (user_id);


--
-- TOC entry 3250 (class 1259 OID 208992)
-- Name: idx_orders_user_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_orders_user_id ON public.orders USING btree (user_id);


--
-- TOC entry 3253 (class 1259 OID 208993)
-- Name: idx_product_attributes_product_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_product_attributes_product_id ON public.product_attributes USING btree (product_id);


--
-- TOC entry 3256 (class 1259 OID 208994)
-- Name: idx_product_category_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_product_category_id ON public.products USING btree (category_id);


--
-- TOC entry 3270 (class 1259 OID 209033)
-- Name: idx_user_address_user_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_user_address_user_id ON public.user_addresses USING btree (user_id);


--
-- TOC entry 3263 (class 1259 OID 208996)
-- Name: schema_version_s_idx; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX schema_version_s_idx ON public.schema_version USING btree (success);


--
-- TOC entry 3273 (class 2606 OID 208997)
-- Name: cart_items cart_items_cart_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_cart_id_fkey FOREIGN KEY (cart_id) REFERENCES public.carts(id);


--
-- TOC entry 3274 (class 2606 OID 209002)
-- Name: cart_items cart_items_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3275 (class 2606 OID 209007)
-- Name: order_items order_items_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- TOC entry 3276 (class 2606 OID 209012)
-- Name: order_items order_items_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3277 (class 2606 OID 209017)
-- Name: product_attributes product_attributes_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.product_attributes
    ADD CONSTRAINT product_attributes_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3278 (class 2606 OID 209022)
-- Name: products products_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.categories(id);


--
-- TOC entry 3279 (class 2606 OID 209027)
-- Name: user_addresses user_profile_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_addresses
    ADD CONSTRAINT user_profile_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.user_pool(id);


--
-- TOC entry 3434 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: madhusudhanan
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2024-03-16 17:11:52 IST

--
-- PostgreSQL database dump complete
--

