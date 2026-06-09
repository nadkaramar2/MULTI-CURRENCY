import React, { useState, useEffect, useRef, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { utils, write } from "xlsx";
import { useReactToPrint } from "react-to-print";
import { v4 as uuidv4 } from "uuid";
import { NavLink } from "react-router-dom";

import { Dialog, Transition } from "@headlessui/react";
import { ArrowDownTrayIcon } from "@heroicons/react/24/outline";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintationReport from "./AutoPagintationReport";

export default function Chart_Accounts() {
  const [alldata, setAlldata] = useState({});
  let balance = alldata.strAllGLBalance;
 const [searchQuery, setSearchQuery] = useState("");
    const [itemPage, setItemPage] = useState(20);
    const [tcount, setTcount] = useState(1);
  const [checked, setChecked] = useState(true);
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  useEffect(() => {
    Balance();
  }, {});

  const Balance = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type-creation/allGLAccountBalanc`,
        {}
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.glAccountCreation);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  // Table
  const [list, setList] = useState([]);
  useEffect(() => {
    handleSubmit();
  }, [tcount, itemPage]);

  const handleSubmit = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type/getGLAcountList/pagination/${itemPage}/${tcount}`,

        {}
      );
      if (response.data.code === "S0000") {
        setList(response.data.glAccountviewModels);
        setChecked(true);
      } else {
        handleShowError(response.data.message);
        setChecked(false);
      }
    } catch (error) {
      // handleShowError(
      //   "Sorry, the server is under mentaines. Please try again later"
      // );
      setChecked(false);
    }
  };


    useEffect(() => {
      if (searchQuery === "" || searchQuery === " ") {
          handleSubmit();
      }
    }, [searchQuery]);

  const Search = async () => {
      try {
      const response = await amsApi.post(
        `gl-account-type/chartAcctSearch/pagination/${itemPage}/${tcount}`,
        {
          keyword: searchQuery,
        }
      );
      if (response.data.code === "S0000") {
        setList(response.data.chartAccountList);
        // handleShowSuccess(response.data.message);
       
        } else {
          handleShowError(response.data.message);
         
        }
      } catch (error) {
        // handleShowError(error.response.data.message);
      }
    
  };

  // Download pdf and Excel
  const conponentPDF = useRef();
  const generatePDF = useReactToPrint({
    content: () => conponentPDF.current,
    documentTitle: "TableData",
  });

  const generateExcel = (list) => {
    const worksheet = utils.json_to_sheet(list);
    const workbook = { Sheets: { list: worksheet }, SheetNames: ["list"] };
    const excelBuffer = write(workbook, { bookType: "xlsx", type: "array" });
    const blob = new Blob([excelBuffer], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "chart_of_accounts.xlsx";
    a.click();
  };

  // Open Model Function
  let [isOpen, setIsOpen] = useState(false);

  function closeModal() {
    setIsOpen(false);
  }
  function openModal() {
    closeModal();
    setIsOpen(true);
  }

  const [list1, setList1] = useState("");
  useEffect(() => {
    poolaccount();
  }, []);

  const poolaccount = async () => {
    try {
      const response = await amsApi.get(
        `thirdParty/NGN/middleWare/getPoolAccountInfo`,
        {}
      );
      if (response.data.code === "S0000") {
        setList1(response.data.poolAccountBalance);
        setChecked(true);
      } else {
        // handleShowError(response.data.message);
        setChecked(false);
      }
    } catch (error) {
      // handleShowError(
      //   "Sorry, the server is under mentaines. Please try again later"
      // );
      setChecked(false);
    }
  };

  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order

  const handleSort = (column) => {
    if (column === sortColumn) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  const renderSortArrow = (column) => {
    return (
      <span className="cursor-pointer   text-md hover:bg-blue-500  hover:text-white px-1 mx-1">
        {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
      </span>
    );
  };

  // Sort the data based on the chosen column and order
  const sortedData = [...list].sort((a, b) => {
    if (sortOrder === "asc") {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.localeCompare(aValue);
    }
  });

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-800">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start  text-white text-md  font-semibold leading-normal">
                Chart of Accounts
              </p>

              <div>
                <button className="inline-flex sm:ml-3 sm:mt-0 items-start justify-start px-3  focus:outline-none rounded text-xs font-normal">
                  <Switch
                    checked={checked}
                    onChange={(event) => setChecked(event.target.checked)}
                    slotProps={{
                      track: {
                        children: (
                          <React.Fragment>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ ml: "10px" }}
                            >
                              On
                            </Typography>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ mr: "8px" }}
                            >
                              Off
                            </Typography>
                          </React.Fragment>
                        ),
                      },
                    }}
                    sx={{
                      "--Switch-thumbSize": "27px",
                      "--Switch-trackWidth": "64px",
                      "--Switch-trackHeight": "31px",
                    }}
                  />
                </button>
              </div>
            </div>
          </div>
        </div>

        {checked && (
          <div className=" md:col-span-2 lg:col-span-1 ">
            <form className="">
              <div className="overflow-hidden shadow ">
                <div className=" px-4  bg-white">
                  <div className="grid gap-4 md:grid-cols-3 lg:grid-cols-4 px-6">
                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Control Account Balance
                        {/* <span className="text-red-600 px-2">*</span> */}
                      </label>
                      <input
                        id="accountType"
                        name="accountType"
                        defaultValue={balance}
                        disabled
                        className="bg-gray-100 block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Pool Account Balance
                        {/* <span className="text-red-600 px-2">*</span> */}
                      </label>
                      <input
                        id="accountType"
                        name="accountType"
                        defaultValue={list1}
                        disabled
                        className="bg-gray-100 block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      />
                    </div>

                    <div className="">
                      <button
                        title="Click to Download Button"
                        onClick={() => openModal()}
                        className="bg-green-500  hover:bg-green-700 focus:ring-2 focus:ring-green-800   focus:ring-offset-2     text-white font-md  py-2 px-5 my-4 border border-green-700 rounded  shadow-lg inner"
                      >
                        <ArrowDownTrayIcon className="h-3 w-3" />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        )}

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={list[0]?.strTotalCount}
          reCallApi={handleSubmit}
        /> */}

        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={list[0]?.strTotalCount}
          reCallApi={Search}
        />
        <div className="overflow-x-auto relative shadow-md ">
          <div className="table-wrp block max-h-[32rem] ">
            <div ref={conponentPDF} style={{ width: "100%" }}>
              <table className="w-full text-xs  text-left text-black dark:text-blue-100">
                <thead className="w-full text-white border-b sticky top-0 bg-blue-500 ">
                  {" "}
                  <tr>
                    <th
                      scope="col"
                      className="py-1 px-6 whitespace-nowrap  "
                      onClick={() => handleSort("strAccountType")}
                    >
                      Type of Account {renderSortArrow("strAccountType")}
                    </th>
                    <th
                      scope="col"
                      className="py-1 px-6 whitespace-nowrap"
                      onClick={() => handleSort("strGLAccountType")}
                    >
                      GL Account Type {renderSortArrow("strGLAccountType")}
                    </th>
                    <th
                      scope="col"
                      className="py-1 px-6 whitespace-nowrap"
                      onClick={() => handleSort("strAccountNumber")}
                    >
                      GL Account Number {renderSortArrow("strAccountNumber")}
                    </th>
                    <th
                      scope="col"
                      className="py-1 px-6 whitespace-nowrap"
                      onClick={() => handleSort("strGLAccountDescription")}
                    >
                      GL Account Description{" "}
                      {renderSortArrow("strGLAccountDescription")}
                    </th>
                    <th
                      scope="col"
                      className="py-1 px-6 text-right whitespace-nowrap"
                      onClick={() => handleSort("strClosingBalance")}
                    >
                      Closing Balance {renderSortArrow("strClosingBalance")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData.map(
                        ({
                          strGLAccountType,
                          strAccountNumber,
                          strGLAccountDescription,
                          strClosingBalance,
                          strAccountType,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className="border-b dark:border-neutral-500"
                          >
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {strAccountType || "_"}
                              </div>
                            </td>
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {strGLAccountType || "_"}
                              </div>
                            </td>
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                <NavLink
                                  to={`/GlAccountsub/${strGLAccountType}/${strAccountNumber}`}
                                  className="border-blue-300 shadow-md p-2  text-blue-700   rounded-full focus:ring-blue-400"
                                >
                                  {strAccountNumber || "-"}
                                </NavLink>
                                {""}
                              </div>
                            </td>
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="text-sm font-medium text-gray-900">
                                {strGLAccountDescription}
                              </div>
                            </td>
                            <td className="px-6 py-1 whitespace-nowrap text-right">
                              <div className="text-sm font-medium text-gray-900 }">
                                {strClosingBalance}
                              </div>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="6" className="px-6 py-1 text-center">
                        <span className="text-lg font-medium text-gray-500">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
        {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
      </div>

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}

      {/* Delete model */}
      <Transition appear show={isOpen} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={closeModal}>
          <Transition.Child
            as={Fragment}
            enter="ease-out duration-300"
            enterFrom="opacity-0"
            enterTo="opacity-100"
            leave="ease-in duration-200"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
          >
            <div className="fixed inset-0 bg-black bg-opacity-25" />
          </Transition.Child>

          <div className="fixed inset-0 overflow-y-auto">
            <div className="flex min-h-full items-center justify-center p-4 text-center">
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0 scale-95"
                enterTo="opacity-100 scale-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100 scale-100"
                leaveTo="opacity-0 scale-95"
              >
                <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                  <Dialog.Title
                    as="h3"
                    className="text-lg font-medium leading-6 text-gray-900"
                  >
                    Download
                  </Dialog.Title>
                  <div className="mt-2">
                    <p className="text-sm text-gray-500">
                      Are you sure you want to download file's ?
                    </p>
                  </div>
                  <div className=" px-4 py-1 sm:px-6 sm:flex sm:flex-row-reverse">
                    <button
                      title="Click Here To Download Pdf"
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-1 bg-green-600 text-sm font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-sm"
                      // onClick={generatePDF}
                      onClick={() => generatePDF(list)}
                    >
                      PDF
                    </button>
                    <button
                      title="Clcik Here To Download Excel "
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-1 bg-orange-600 text-sm font-medium text-white hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-sm"
                      onClick={() => generateExcel(list)}
                    >
                      Excel
                    </button>
                    <button
                      title="Cancel Here"
                      type="button"
                      className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-1 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
                      onClick={closeModal}
                    >
                      Cancel
                    </button>
                  </div>
                </Dialog.Panel>
              </Transition.Child>
            </div>
          </div>
        </Dialog>
      </Transition>
    </AppLayout>
  );
}
