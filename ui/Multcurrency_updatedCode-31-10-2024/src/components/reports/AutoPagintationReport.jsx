import React, { useState, useEffect } from "react";
import { ArrowLeftIcon, ArrowRightIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
const AutoPagintationReport = (props) => {
  const [searchQuery, setSearchQuery] = useState("");
  const TotalPageNo = Math.floor(
    parseInt(props.tcount) / parseInt(props.pagePerData) + 1
  );
  const itemPerPageHandler = (event) => {
    props.addPageperData(parseInt(event.target.value));
    props.addCurrentPage(1);
  };
  useEffect(() => {
    props.searchData(searchQuery);
  }, [searchQuery]);
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

  function handleSubmit() {
    props.reCallApi();
  }
  return (
    <div className=" px-4 flex items-center justify-between  sm:px-6">
      <div className="flex-1 flex justify-between sm:hidden">
        <button
          onClick={prevPage}
          className="relative inline-flex items-center px-4 py-1 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
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
      <div className="sm:flex-1 sm:flex sm:items-center sm:justify-between">
        <div className=" relative text-gray-600">
          {/* <span type="submit" className="absolute left-0 top-0 mt-1 px-1 ">
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
                d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
              />
            </svg>
          </span> */}
          <div className=" flex gap-4 py-0.5">
            <input
              title="Search Data"
              type="search"
              id="search"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="block w-60 pl-4 pr-1 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border-b border-solid border-gray-400 rounded  m-0"
              placeholder="Search.."
              autoComplete="off"
            />

            <button
              title="Click Search Button"
              type="submit"
              onClick={handleSubmit}
              data-modal-toggle="defaultModal"
              className="bg-blue-400 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800   focus:ring-offset-2 text-white font-sm px-2 py-0.5 border border-blue-700 rounded  shadow-md inner"
            >
              <MagnifyingGlassIcon className="h-5 w-5" />
            </button>
          </div>
        </div>
        <div className="flex flex-row items-center">
          <div className="">
            <label htmlFor="" className="text-xs  font-medium">
              Records Per Page
            </label>
            <select
              onChange={itemPerPageHandler}
              className="focus:ring-indigo-500 focus:border-indigo-500 mx-2 h-full py-0 px-1 border border-gray-900 bg-transparent text-gray-900  text-xs rounded-md"
            >
              <option value="20">20</option>
              <option value="25">25</option>
              <option value="50">50</option>
              <option value="75">75</option>
              <option value="100">100</option>
            </select>
          </div>
          <div>
            <nav
              className="relative z-0 inline-flex rounded-md shadow-sm   items-center -space-x-px"
              aria-label="Pagination"
            >
              <div>
                <p className="text-xs  font-medium  mx-3">
                  {/* Current Page */}
                  <span className="  mx-2 text-xs font-medium   p-1 rounded-sm bg-white ">
                    {props.currentPage}
                  </span>
                  <span>of</span>
                  <span className="text-xs  font-medium mx-2   p-1 rounded-sm bg-white">
                    {TotalPageNo || 0}
                  </span>
                  {/* Total Page */}
                </p>
              </div>
              <div className=" flex justify-center">
                <button
                  onClick={prevPage}
                  className="items-center  px-1 py-1  rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                >
                  <span className="sr-only">Previous</span>
                  <ArrowLeftIcon className="h-4 w-4 " aria-hidden="true" />
                </button>

                <button
                  onClick={nextpage}
                  className=" ml-1 items-center px-1 py-1 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
                >
                  <span className="sr-only">Next</span>
                  <ArrowRightIcon className="h-4 w-4" aria-hidden="true" />
                </button>
              </div>
            </nav>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AutoPagintationReport;
