import React, { useState } from "react";
export default function CreditLimitList(props) {
  const [page, setPage] = useState("");
  const [frompage, setFromPage] = useState("");
  const [topage, setToPage] = useState("");

  const itemPerPageHandler = (e) => {
    setPage(e.target.value);
    if (e.target.value === "custom") {
      return null;
    }
    props.addPageperData(parseInt(e.target.value));
    props.addCurrentPage(1);
  };

  const customItemPerPageHandler = () => {
    props.addPageperData(parseInt(topage));
    props.addCurrentPage(frompage);
  };

  const TotalPageNo = Math.floor(
    parseInt(props.tcount) / parseInt(props.pagePerData) + 1
  );

  //   const itemPerPageHandler = (e) => {
  //     props.addPageperData(parseInt(e.target.value));
  //     props.addCurrentPage(1);
  //   };
  function nextpage() {
    if (TotalPageNo > props.currentPage) {
      props.addCurrentPage(props.currentPage + 1);
    }
  }
  function prevPage() {
    if (props.currentPage > 1) {
      props.addCurrentPage(props.currentPage - 1);
    }
  }

  return (
    <div className="bg-blue-200 px-4 py-3 flex items-center justify-between  sm:px-6">
      <div className="flex-1 flex justify-between sm:hidden">
        <button
          onClick={prevPage}
          className="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
        >
          Previous
        </button>
        <button
          onClick={nextpage}
          className="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
        >
          Next
        </button>
      </div>
      <div className="  sm:flex-1 sm:flex sm:items-center sm:justify-between">
        <div className="mx-2">
          <label htmlFor="" className="text-sm text-gray-700">
            Items Per Page
          </label>
          <select
            onChange={itemPerPageHandler}
            value={page}
            className="focus:ring-indigo-500 focus:border-indigo-500 mx-2 h-full py-0 px-4 border border-gray-900 bg-transparent text-gray-900 text-base rounded-md"
          >
            <option value="10">10</option>
            <option value="25">25</option>
            <option value="50">50</option>
            <option value="75">75</option>
            <option value="100">100</option>
            <option value="custom">{"Custom"}</option>
          </select>
        </div>
        <div className="lg:mr-64">
          {page === "custom" && (
            <>
              <div className="grid lg:grid-cols-3 md:grid-cols-3 sm:grid-cols-3 gap-4">
                <div className="">
                  <input
                    type="text"
                    name="custom"
                    id="custom"
                    autoComplete="custom"
                    value={frompage}
                    onChange={(e) => setFromPage(e.target.value)}
                    className="border border-black text-black text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-black dark:border-black dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="From Page"
                    required
                  />
                </div>
                <div className="">
                  <input
                    type="text"
                    name="custom"
                    id="custom"
                    autoComplete="custom"
                    value={topage}
                    onChange={(e) => setToPage(e.target.value)}
                    className="border border-black text-black text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-black dark:border-black dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder="To Page"
                    required
                  />
                </div>
                <div className="">
                  <button
                    type="submit"
                    onClick={customItemPerPageHandler}
                    className="inline-flex justify-right whitespace-nowrap py-2 px-8 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-400 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-400"
                  >
                    Submit
                  </button>
                </div>
              </div>
            </>
          )}
        </div>
        <div>
          <div
            className="relative z-0 inline-flex rounded-md shadow-sm   items-center -space-x-px"
            aria-label="Pagination"
          >
            <div>
              <p className="text-base text-gray-900  mx-3">
                Showing
                <span className="font-bold  mx-2 text-lg text-blue-700 ">
                  {props.currentPage}
                </span>
                <span>of</span>
                <span className="font-bold mx-2 text-lg text-blue-700">
                  {/* {TotalPageNo} */}
                  {props.pagePerData}
                </span>
                Results
              </p>
            </div>
            {/* <div className=" space-x-2">
              <button
                onClick={prevPage}
                className="relative inline-flex items-center px-2 py-2 rounded-l-md border shadow-xl border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
              >
                <span className="sr-only">Previous</span>
                <ChevronLeftIcon className="h-5 w-5" aria-hidden="true" />
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-5 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M15.75 19.5L8.25 12l7.5-7.5"
                  />
                </svg>
              </button>
              <button
                onClick={nextpage}
                className="relative inline-flex items-center px-2 py-2 rounded-r-md border shadow-xl border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
              >
                <span className="sr-only">Next</span>
                <ChevronRightIcon className="h-5 w-5" aria-hidden="true" />
              </button>
            </div> */}
          </div>
        </div>
      </div>
    </div>
  );
}
