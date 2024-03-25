--
-- PostgreSQL database dump
--

-- Dumped from database version 13.11
-- Dumped by pg_dump version 15.3

-- Started on 2024-03-20 22:11:20 IST

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
-- TOC entry 200 (class 1259 OID 220352)
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
-- TOC entry 201 (class 1259 OID 220361)
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
-- TOC entry 3473 (class 0 OID 0)
-- Dependencies: 201
-- Name: cart_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.cart_items_id_seq OWNED BY public.cart_items.id;


--
-- TOC entry 202 (class 1259 OID 220363)
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
-- TOC entry 203 (class 1259 OID 220371)
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
-- TOC entry 3474 (class 0 OID 0)
-- Dependencies: 203
-- Name: carts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.carts_id_seq OWNED BY public.carts.id;


--
-- TOC entry 204 (class 1259 OID 220373)
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
-- TOC entry 205 (class 1259 OID 220381)
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
-- TOC entry 3475 (class 0 OID 0)
-- Dependencies: 205
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.categories_id_seq OWNED BY public.categories.id;


--
-- TOC entry 206 (class 1259 OID 220383)
-- Name: delivery_details; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.delivery_details (
    id bigint NOT NULL,
    order_id bigint NOT NULL,
    customer_email text,
    customer_name text,
    shipping_address_1 text,
    shipping_address_2 text,
    shipping_city text,
    shipping_state text,
    shipping_zip_code text,
    billing_address_1 text,
    billing_address_2 text,
    billing_city text,
    billing_state text,
    billing_zip_code text,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.delivery_details OWNER TO madhusudhanan;

--
-- TOC entry 207 (class 1259 OID 220391)
-- Name: delivery_details_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.delivery_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.delivery_details_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 207
-- Name: delivery_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.delivery_details_id_seq OWNED BY public.delivery_details.id;


--
-- TOC entry 208 (class 1259 OID 220393)
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
    tax_percentage double precision DEFAULT 0.00,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status text NOT NULL
);


ALTER TABLE public.order_items OWNER TO madhusudhanan;

--
-- TOC entry 209 (class 1259 OID 220406)
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
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 209
-- Name: order_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.order_items_id_seq OWNED BY public.order_items.id;


--
-- TOC entry 210 (class 1259 OID 220408)
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
-- TOC entry 211 (class 1259 OID 220418)
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
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 211
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- TOC entry 212 (class 1259 OID 220420)
-- Name: payments; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.payments (
    id bigint NOT NULL,
    order_id bigint NOT NULL,
    payment_mode text NOT NULL,
    status text NOT NULL,
    amount double precision DEFAULT 0.00 NOT NULL,
    payment_date timestamp with time zone,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    transaction_id text
);


ALTER TABLE public.payments OWNER TO madhusudhanan;

--
-- TOC entry 213 (class 1259 OID 220429)
-- Name: payments_id_seq; Type: SEQUENCE; Schema: public; Owner: madhusudhanan
--

CREATE SEQUENCE public.payments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.payments_id_seq OWNER TO madhusudhanan;

--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 213
-- Name: payments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.payments_id_seq OWNED BY public.payments.id;


--
-- TOC entry 214 (class 1259 OID 220431)
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
-- TOC entry 215 (class 1259 OID 220439)
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
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 215
-- Name: product_attributes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.product_attributes_id_seq OWNED BY public.product_attributes.id;


--
-- TOC entry 216 (class 1259 OID 220441)
-- Name: products; Type: TABLE; Schema: public; Owner: madhusudhanan
--

CREATE TABLE public.products (
    id bigint NOT NULL,
    category_id bigint NOT NULL,
    name text NOT NULL,
    description text,
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
-- TOC entry 217 (class 1259 OID 220454)
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
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 217
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- TOC entry 218 (class 1259 OID 220456)
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
-- TOC entry 219 (class 1259 OID 220463)
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
-- TOC entry 220 (class 1259 OID 220471)
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
-- TOC entry 221 (class 1259 OID 220479)
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
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 221
-- Name: user_pool_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.user_pool_id_seq OWNED BY public.user_pool.id;


--
-- TOC entry 222 (class 1259 OID 220481)
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
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 222
-- Name: user_profile_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: madhusudhanan
--

ALTER SEQUENCE public.user_profile_id_seq OWNED BY public.user_addresses.id;


--
-- TOC entry 3206 (class 2604 OID 220483)
-- Name: cart_items id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.cart_items ALTER COLUMN id SET DEFAULT nextval('public.cart_items_id_seq'::regclass);


--
-- TOC entry 3210 (class 2604 OID 220484)
-- Name: carts id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.carts ALTER COLUMN id SET DEFAULT nextval('public.carts_id_seq'::regclass);


--
-- TOC entry 3213 (class 2604 OID 220485)
-- Name: categories id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.categories ALTER COLUMN id SET DEFAULT nextval('public.categories_id_seq'::regclass);


--
-- TOC entry 3216 (class 2604 OID 220486)
-- Name: delivery_details id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.delivery_details ALTER COLUMN id SET DEFAULT nextval('public.delivery_details_id_seq'::regclass);


--
-- TOC entry 3219 (class 2604 OID 220487)
-- Name: order_items id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.order_items ALTER COLUMN id SET DEFAULT nextval('public.order_items_id_seq'::regclass);


--
-- TOC entry 3227 (class 2604 OID 220488)
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- TOC entry 3232 (class 2604 OID 220489)
-- Name: payments id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.payments ALTER COLUMN id SET DEFAULT nextval('public.payments_id_seq'::regclass);


--
-- TOC entry 3236 (class 2604 OID 220490)
-- Name: product_attributes id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.product_attributes ALTER COLUMN id SET DEFAULT nextval('public.product_attributes_id_seq'::regclass);


--
-- TOC entry 3239 (class 2604 OID 220491)
-- Name: products id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- TOC entry 3248 (class 2604 OID 220492)
-- Name: user_addresses id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_addresses ALTER COLUMN id SET DEFAULT nextval('public.user_profile_id_seq'::regclass);


--
-- TOC entry 3251 (class 2604 OID 220493)
-- Name: user_pool id; Type: DEFAULT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_pool ALTER COLUMN id SET DEFAULT nextval('public.user_pool_id_seq'::regclass);


--
-- TOC entry 3444 (class 0 OID 220352)
-- Dependencies: 200
-- Data for Name: cart_items; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.cart_items (id, cart_id, product_id, quantity, status, created_at, updated_at) FROM stdin;
\.


--
-- TOC entry 3446 (class 0 OID 220363)
-- Dependencies: 202
-- Data for Name: carts; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.carts (id, user_id, status, created_at, updated_at) FROM stdin;
2	dfac34a6-9994-42a4-be3c-86e568cb65c8	UN_LOCKED	2024-03-16 15:54:56.94977+05:30	2024-03-16 15:54:56.94977+05:30
1	2d309cdb-8911-4e62-99d8-00ddefbdc3d1	UN_LOCKED	2024-03-16 15:54:56.94977+05:30	2024-03-18 20:15:57.668675+05:30
\.


--
-- TOC entry 3448 (class 0 OID 220373)
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
-- TOC entry 3450 (class 0 OID 220383)
-- Dependencies: 206
-- Data for Name: delivery_details; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.delivery_details (id, order_id, customer_email, customer_name, shipping_address_1, shipping_address_2, shipping_city, shipping_state, shipping_zip_code, billing_address_1, billing_address_2, billing_city, billing_state, billing_zip_code, created_at, updated_at) FROM stdin;
1	25	jane.doe@example.com	Jane Doe	Dark st		Chennai	Tamil Nadu	600100	Bright st		Chennai	Tamil Nadu	345676	2024-03-18 11:51:18.019405+05:30	2024-03-18 11:51:18.019437+05:30
5	29	jane.doe@example.com	Jane Doe	Dark st		Chennai	Tamil Nadu	600100	Bright st		Chennai	Tamil Nadu	345676	2024-03-18 17:26:56.696276+05:30	2024-03-18 17:26:56.696305+05:30
6	30	jane.doe@example.com	Jane Doe	Dark st		Chennai	Tamil Nadu	600100	Bright st		Chennai	Tamil Nadu	345676	2024-03-18 19:54:08.910582+05:30	2024-03-18 19:54:08.910602+05:30
\.


--
-- TOC entry 3452 (class 0 OID 220393)
-- Dependencies: 208
-- Data for Name: order_items; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.order_items (id, order_id, product_id, quantity, unit_price, net_amount, tax_amount, gross_amount, tax_percentage, created_at, updated_at, status) FROM stdin;
12	25	1	1	87000.98	87000.98	10440.12	97441.1	18	2024-03-18 11:51:18.005572+05:30	2024-03-18 13:08:32.725741+05:30	CONFIRMED
15	25	8	7	750.45	5253.15	630.35	5883.5	5	2024-03-18 11:51:18.01112+05:30	2024-03-18 13:08:32.744037+05:30	CONFIRMED
14	25	10	3	750.45	2251.35	270.15	2521.5	5	2024-03-18 11:51:18.009897+05:30	2024-03-18 13:08:32.745655+05:30	CONFIRMED
13	25	11	2	750.45	1500.9	180.1	1681	5	2024-03-18 11:51:18.00824+05:30	2024-03-18 13:08:32.74703+05:30	CONFIRMED
19	29	6	2	400.45	800.9	96.1	897	12	2024-03-18 17:26:56.692474+05:30	2024-03-18 17:56:12.824315+05:30	CONFIRMED
21	30	1	2	87000.98	174001.96	20880.24	194882.2	18	2024-03-18 19:54:08.908873+05:30	2024-03-18 20:15:57.665942+05:30	CONFIRMED
20	30	6	4	400.45	1601.8	192.2	1794	12	2024-03-18 19:54:08.907068+05:30	2024-03-18 20:15:57.667416+05:30	CONFIRMED
\.


--
-- TOC entry 3454 (class 0 OID 220408)
-- Dependencies: 210
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.orders (id, user_id, status, net_total, gross_total, created_at, updated_at) FROM stdin;
25	2d309cdb-8911-4e62-99d8-00ddefbdc3d1	CONFIRMED	96006.38	107527.1	2024-03-18 11:51:17.993025+05:30	2024-03-18 13:08:32.724417+05:30
29	2d309cdb-8911-4e62-99d8-00ddefbdc3d1	CONFIRMED	800.9	897	2024-03-18 17:26:56.67903+05:30	2024-03-18 17:56:12.79702+05:30
30	2d309cdb-8911-4e62-99d8-00ddefbdc3d1	CONFIRMED	175603.76	196676.2	2024-03-18 19:54:08.904846+05:30	2024-03-18 20:15:57.664612+05:30
\.


--
-- TOC entry 3456 (class 0 OID 220420)
-- Dependencies: 212
-- Data for Name: payments; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.payments (id, order_id, payment_mode, status, amount, payment_date, created_at, updated_at, transaction_id) FROM stdin;
1	25	CREDIT_CARD	SUCCESS	107527.1	2024-03-18 13:02:49.739222+05:30	2024-03-18 11:51:18.032301+05:30	2024-03-18 13:02:49.740916+05:30	\N
5	29	CREDIT_CARD	SUCCESS	897	2024-03-18 17:47:08.622939+05:30	2024-03-18 17:26:56.699751+05:30	2024-03-18 17:47:08.62695+05:30	\N
6	30	CREDIT_CARD	SUCCESS	196676.2	2024-03-18 20:14:46.984073+05:30	2024-03-18 19:54:08.912451+05:30	2024-03-18 20:14:46.985106+05:30	\N
\.


--
-- TOC entry 3458 (class 0 OID 220431)
-- Dependencies: 214
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
-- TOC entry 3460 (class 0 OID 220441)
-- Dependencies: 216
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.products (id, category_id, name, description, sku_code, stock_quantity, unit_price, gross_amount, tax_percentage, tax_amount, created_at, updated_at) FROM stdin;
2	1	iPhone 15 128 GB - Lilac	\N	iPho/15/lil/128 	100	87000.98	97441.1	18	10440.12	2024-03-16 15:56:12.743765+05:30	2024-03-16 15:56:12.743797+05:30
3	1	iPhone 15 256 GB - Lilac	\N	iPho/15/lil/256 	100	87000.98	97441.1	18	10440.12	2024-03-16 15:56:33.437581+05:30	2024-03-16 15:56:33.437601+05:30
4	1	iPhone 15 256 GB - Red	\N	iPho/15/r/256 	100	87000.98	97441.1	18	10440.12	2024-03-16 15:56:39.379169+05:30	2024-03-16 15:56:39.379197+05:30
5	4	Moisturiser 50 ml	\N	Mois/50 /Ind/Loti	100	200.45	224.5	12	24.05	2024-03-16 15:58:28.228736+05:30	2024-03-16 15:58:28.228764+05:30
7	4	Moisturiser 200 ml	\N	Mois/200 /Ind/Loti	100	350.45	392.5	12	42.05	2024-03-16 15:59:53.356178+05:30	2024-03-16 15:59:53.356197+05:30
8	2	Shirt Checked - S	\N	Shir/1/Ind/Cott	93	750.45	840.5	5	90.05	2024-03-16 16:49:09.630772+05:30	2024-03-18 13:08:32.91843+05:30
10	2	Checked - M	\N	Chec/1/Ind/Cott	97	750.45	840.5	5	90.05	2024-03-16 16:49:49.564123+05:30	2024-03-18 13:08:32.920582+05:30
11	2	Blue Checked - L	\N	Blue/1/Ind/Cott	98	750.45	840.5	5	90.05	2024-03-16 16:50:07.024056+05:30	2024-03-18 13:08:32.922233+05:30
1	1	iPhone 15 128 GB - Blue	\N	iPho/15/bl/128 	97	87000.98	97441.1	18	10440.12	2024-03-16 15:55:17.861711+05:30	2024-03-18 20:15:57.712999+05:30
6	4	Moisturiser 250 ml	\N	Mois/250 /Ind/Loti	94	400.45	448.5	12	48.05	2024-03-16 15:59:39.044125+05:30	2024-03-18 20:15:57.71514+05:30
\.


--
-- TOC entry 3462 (class 0 OID 220456)
-- Dependencies: 218
-- Data for Name: schema_version; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.schema_version (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
2	2	create order shopping cart tables	SQL	V2__create_order_shopping_cart_tables.sql	-1310596681	madhusudhanan	2024-03-16 15:54:57.077329	82	t
1	1	create tables	SQL	V1__create_tables.sql	735756421	madhusudhanan	2024-03-16 15:54:56.931695	121	t
3	3	create order delivery payment tables	SQL	V3__create_order_delivery_payment_tables.sql	-929485003	madhusudhanan	2024-03-18 11:40:46.812901	50	t
\.


--
-- TOC entry 3463 (class 0 OID 220463)
-- Dependencies: 219
-- Data for Name: user_addresses; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.user_addresses (id, user_id, address_1, address_2, city, state, zip_code, created_at, updated_at, is_default) FROM stdin;
\.


--
-- TOC entry 3464 (class 0 OID 220471)
-- Dependencies: 220
-- Data for Name: user_pool; Type: TABLE DATA; Schema: public; Owner: madhusudhanan
--

COPY public.user_pool (id, cognito_sub, email, first_name, last_name, created_at, updated_at) FROM stdin;
\.


--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 201
-- Name: cart_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.cart_items_id_seq', 9, true);


--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 203
-- Name: carts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.carts_id_seq', 1, false);


--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 205
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.categories_id_seq', 18, true);


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 207
-- Name: delivery_details_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.delivery_details_id_seq', 6, true);


--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 209
-- Name: order_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.order_items_id_seq', 21, true);


--
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 211
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.orders_id_seq', 30, true);


--
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 213
-- Name: payments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.payments_id_seq', 6, true);


--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 215
-- Name: product_attributes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.product_attributes_id_seq', 42, true);


--
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 217
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.products_id_seq', 11, true);


--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 221
-- Name: user_pool_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.user_pool_id_seq', 1, false);


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 222
-- Name: user_profile_id_seq; Type: SEQUENCE SET; Schema: public; Owner: madhusudhanan
--

SELECT pg_catalog.setval('public.user_profile_id_seq', 1, false);


--
-- TOC entry 3255 (class 2606 OID 220495)
-- Name: cart_items cart_items_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_pkey PRIMARY KEY (id);


--
-- TOC entry 3257 (class 2606 OID 220497)
-- Name: cart_items cart_product_unique; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_product_unique UNIQUE (cart_id, product_id);


--
-- TOC entry 3260 (class 2606 OID 220499)
-- Name: carts carts_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT carts_pkey PRIMARY KEY (id);


--
-- TOC entry 3265 (class 2606 OID 220501)
-- Name: categories categories_name_key; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_name_key UNIQUE (name);


--
-- TOC entry 3267 (class 2606 OID 220503)
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- TOC entry 3269 (class 2606 OID 220505)
-- Name: delivery_details delivery_details_order_id; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.delivery_details
    ADD CONSTRAINT delivery_details_order_id UNIQUE (order_id);


--
-- TOC entry 3271 (class 2606 OID 220507)
-- Name: delivery_details delivery_details_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.delivery_details
    ADD CONSTRAINT delivery_details_pkey PRIMARY KEY (id);


--
-- TOC entry 3274 (class 2606 OID 220509)
-- Name: order_items order_items_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_pkey PRIMARY KEY (id);


--
-- TOC entry 3276 (class 2606 OID 220511)
-- Name: order_items order_items_unique; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_unique UNIQUE (order_id, product_id);


--
-- TOC entry 3279 (class 2606 OID 220513)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 3282 (class 2606 OID 220515)
-- Name: payments payments_order_id; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_order_id UNIQUE (order_id);


--
-- TOC entry 3284 (class 2606 OID 220517)
-- Name: payments payments_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);


--
-- TOC entry 3287 (class 2606 OID 220519)
-- Name: product_attributes product_attributes_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.product_attributes
    ADD CONSTRAINT product_attributes_pkey PRIMARY KEY (id);


--
-- TOC entry 3290 (class 2606 OID 220521)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 3292 (class 2606 OID 220523)
-- Name: products products_sku_code_key; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_sku_code_key UNIQUE (sku_code);


--
-- TOC entry 3294 (class 2606 OID 220525)
-- Name: schema_version schema_version_pk; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.schema_version
    ADD CONSTRAINT schema_version_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 3263 (class 2606 OID 220527)
-- Name: carts user_cart_unique; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT user_cart_unique UNIQUE (user_id);


--
-- TOC entry 3300 (class 2606 OID 220529)
-- Name: user_pool user_pool_cognito_sub_key; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_pool
    ADD CONSTRAINT user_pool_cognito_sub_key UNIQUE (cognito_sub);


--
-- TOC entry 3302 (class 2606 OID 220531)
-- Name: user_pool user_pool_email_key; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_pool
    ADD CONSTRAINT user_pool_email_key UNIQUE (email);


--
-- TOC entry 3304 (class 2606 OID 220533)
-- Name: user_pool user_pool_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_pool
    ADD CONSTRAINT user_pool_pkey PRIMARY KEY (id);


--
-- TOC entry 3298 (class 2606 OID 220535)
-- Name: user_addresses user_profile_pkey; Type: CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_addresses
    ADD CONSTRAINT user_profile_pkey PRIMARY KEY (id);


--
-- TOC entry 3258 (class 1259 OID 220536)
-- Name: idx_cart_items_cart_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_cart_items_cart_id ON public.cart_items USING btree (cart_id);


--
-- TOC entry 3261 (class 1259 OID 220537)
-- Name: idx_carts_user_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_carts_user_id ON public.carts USING btree (user_id);


--
-- TOC entry 3272 (class 1259 OID 220538)
-- Name: idx_delivery_details_order_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_delivery_details_order_id ON public.delivery_details USING btree (order_id);


--
-- TOC entry 3277 (class 1259 OID 220539)
-- Name: idx_orders_user_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_orders_user_id ON public.orders USING btree (user_id);


--
-- TOC entry 3280 (class 1259 OID 220540)
-- Name: idx_payments_order_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_payments_order_id ON public.payments USING btree (order_id);


--
-- TOC entry 3285 (class 1259 OID 220541)
-- Name: idx_product_attributes_product_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_product_attributes_product_id ON public.product_attributes USING btree (product_id);


--
-- TOC entry 3288 (class 1259 OID 220542)
-- Name: idx_product_category_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_product_category_id ON public.products USING btree (category_id);


--
-- TOC entry 3296 (class 1259 OID 220543)
-- Name: idx_user_address_user_id; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX idx_user_address_user_id ON public.user_addresses USING btree (user_id);


--
-- TOC entry 3295 (class 1259 OID 220544)
-- Name: schema_version_s_idx; Type: INDEX; Schema: public; Owner: madhusudhanan
--

CREATE INDEX schema_version_s_idx ON public.schema_version USING btree (success);


--
-- TOC entry 3305 (class 2606 OID 220545)
-- Name: cart_items cart_items_cart_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_cart_id_fkey FOREIGN KEY (cart_id) REFERENCES public.carts(id);


--
-- TOC entry 3306 (class 2606 OID 220550)
-- Name: cart_items cart_items_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3307 (class 2606 OID 220555)
-- Name: delivery_details delivery_details_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.delivery_details
    ADD CONSTRAINT delivery_details_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- TOC entry 3308 (class 2606 OID 220560)
-- Name: order_items order_items_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- TOC entry 3309 (class 2606 OID 220565)
-- Name: order_items order_items_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3310 (class 2606 OID 220570)
-- Name: payments payments_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- TOC entry 3311 (class 2606 OID 220575)
-- Name: product_attributes product_attributes_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.product_attributes
    ADD CONSTRAINT product_attributes_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 3312 (class 2606 OID 220580)
-- Name: products products_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.categories(id);


--
-- TOC entry 3313 (class 2606 OID 220585)
-- Name: user_addresses user_profile_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: madhusudhanan
--

ALTER TABLE ONLY public.user_addresses
    ADD CONSTRAINT user_profile_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.user_pool(id);


--
-- TOC entry 3472 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: madhusudhanan
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2024-03-20 22:11:20 IST

--
-- PostgreSQL database dump complete
--

